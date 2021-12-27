package cn.zhuguoqing.operationLog.aop.strategy;


import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import cn.zhuguoqing.operationLog.bean.dto.OperationLogDTO;
import cn.zhuguoqing.operationLog.bean.dto.Operator;
import cn.zhuguoqing.operationLog.bean.dto.RecordExecutorDTO;
import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import cn.zhuguoqing.operationLog.support.util.SnowFlakeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author:guoqing.zhu @Date：2021/11/26 15:37 @Desription: TODO @Version: 1.0
 */
@Component
@Slf4j
public class ComplexUpdateStrategy extends AbstractOperationLogStrategy {

    @SneakyThrows
    @Override
    public Object operate(ProceedingJoinPoint p, OperationLog operationlog) {
        // 0.仔细思考没必要使用这玩意,栈都是线程独享的,先留着把,可以再优化;
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        // 1.上下文对象放入空
        LogRecordContext.putEmptySpan();
        LogRecordContext.putVariable("logId", String.valueOf(SnowFlakeUtil.getId()));
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
        OperationLogDTO operationLogDTO = null;
        Map<String, String> functionNameAndReturnMap = null;
        Map<String, Object> columnCommentMap = null;
        String[] cloum = new String[0];
        String assembleSQL = null;
        Map<String, Object> oldMap = null;
        try {
            // 2.解析注解获取实体
            operationLogDTO = logRecordOperationSourceParser.parseLogRecordAnnotation(operationlog);
            // 3.获取要解析的模板
            List<String> beforeSpElTemplates = getProcessBeforeExcuteTemplate(operationLogDTO);
            // 3.获取要解析的模板
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operationLogDTO);
            // 4.解析procceed之前的spEL模板,主要用于获取参数
            Map<String, String> templateReturnMap = processBeforeExecuteTemplate(beforeSpElTemplates, getTargetClass(p.getTarget()), getMethod(p), p.getArgs());
            // 4.解析procceed之前的spEL模板,主要用于获取自定义方法
            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, getTargetClass(p.getTarget()), getMethod(p), p.getArgs());
            // 5.获取数据库的中文字段
            columnCommentMap = operationLogService.getColumnCommentMap(operationlog.table());
            // 6.如果注解的cloum属性没有值,那么就取数据库的值
            cloum = operationlog.includeColumn();
            String[] excludeColumn = operationlog.excludeColumn();
            cloum = getColumIfColumAnnoIsNull(columnCommentMap, cloum, excludeColumn);
            // 7.拼接根据id获取到旧值;
            assembleSQL = operationLogService.getAssembleSQL(operationLogDTO.getTable(), operationLogDTO.getIdName(), templateReturnMap.get(operationLogDTO.getIdRef()), cloum);
            oldMap = iOperationLogService.selectAnyTable(assembleSQL);
            if (CollectionUtils.isEmpty(oldMap)) {
                throw new IllegalArgumentException("查询出来的数据为空:SQL为:" + assembleSQL);
            }
        } catch (Exception e) {
            atomicBoolean.compareAndSet(true, false);
            debugProcessor.error("log record parse before function exception", e);
        }
        // 8.执行切点
        Object proceed = null;
        Throwable throwable = null;
        try {
            proceed = p.proceed();
            //拓展点,检测响应结果
            checkResult.checkResult(methodExecuteResult,proceed);
        } catch (Throwable e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage() == null ? "系统异常,请查看具体日志" : e.getMessage());
            debugProcessor.error("proceed exception", e);
            throwable = e;
        }

        try {
            if (atomicBoolean.get()) {
                // 9.切点执行完,表示数据库已经更新完成,执行相同的SQL查询修改的数据
                Map<String, Object> newMap = iOperationLogService.selectAnyTable(assembleSQL);
                // 10.执行记录
                RecordExecutorDTO recordExecutorDTO = RecordExecutorDTO.builder()
                        .oldMap(oldMap)
                        .newMap(newMap)
                        .ret(proceed)
                        .method(getMethod(p))
                        .args(p.getArgs())
                        .operationLogDTO(operationLogDTO)
                        .targetClass(getTargetClass(p.getTarget()))
                        .success(methodExecuteResult.isSuccess())
                        .errorMsg(methodExecuteResult.getErrorMsg())
                        .functionNameAndReturnMap(functionNameAndReturnMap)
                        .columnCommentMap(columnCommentMap)
                        .cloum(cloum)
                        .schemaTable(operationlog.table())
                        .build();
                if (Objects.nonNull(operationLogDTO)) {
                    recordExecute(recordExecutorDTO);
                }
            }
        } catch (Exception t) {
            // 记录日志错误不要影响业务
            debugProcessor.error("log record parse exception", t);
        } finally {
            LogRecordContext.clear();
            atomicBoolean.set(true);
        }
        //重新抛出异常
        if (throwable != null) {
            throw throwable;
        }
        return proceed;


    }


    /**
     * @param dto
     */
    private void recordExecute(RecordExecutorDTO dto) {
        try {
            String action = getActionContent(dto.getSuccess(), dto.getOperationLogDTO());
            // 获取需要解析的表达式
            List<String> spElTemplates = getSpElTemplates(dto.getOperationLogDTO(), action);
            Map<String, String> expressionValues = processTemplate(spElTemplates, dto.getRet(), dto.getTargetClass(), dto.getMethod(),
                    dto.getArgs(), dto.getErrorMsg(), dto.getFunctionNameAndReturnMap());
            // condition is true
            if (logConditionPassed(dto.getOperationLogDTO().getCondition(), expressionValues)) {
                Operator operator = getOperator();
                operationLogService.insertLogAndLogDetail(dto.getSchemaTable(), dto.getOldMap(), dto.getNewMap(), dto.getOperationLogDTO(),
                        dto.getCloum(), dto.getColumnCommentMap(), expressionValues, action, operator, dto.getSuccess());
            }
        } catch (Exception t) {
            log.error("log record execute exception", t);
        }
    }

    @Override
    public OperationType getOperationLogType() {
        return OperationType.COMPLEX_UPDATE;
    }
}
