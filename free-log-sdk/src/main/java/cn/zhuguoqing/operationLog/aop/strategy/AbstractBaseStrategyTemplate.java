package cn.zhuguoqing.operationLog.aop.strategy;


import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import cn.zhuguoqing.operationLog.bean.dto.OperationLogDTO;
import cn.zhuguoqing.operationLog.bean.dto.Operator;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import cn.zhuguoqing.operationLog.support.util.SnowFlakeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/2 21:29
 * @Desription: 基础模板
 * @Version: 1.0
 */
@Slf4j
public abstract class AbstractBaseStrategyTemplate extends AbstractOperationLogStrategy {

    @SneakyThrows
    @Override
    public Object operate(ProceedingJoinPoint p, OperationLog operationlog) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        // 1.上下文对象放入空
        LogRecordContext.putEmptySpan();
        LogRecordContext.putVariable("logId", String.valueOf(SnowFlakeUtil.getId()));
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
        // 2.解析注解获取实体
        OperationLogDTO operationLogDTO = null;
        Map<String, String> functionNameAndReturnMap = null;
        try {
            operationLogDTO = logRecordOperationSourceParser.parseLogRecordAnnotation(operationlog);
            // 3.获取要解析的模板
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operationLogDTO);
            // 4.解析procceed之前的spEL模板,主要用于获取自定义方法
            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, getTargetClass(p.getTarget()), getMethod(p), p.getArgs());
        } catch (Exception e) {
            atomicBoolean.compareAndSet(true, false);
            debugProcessor.error("log record parse before function exception", e);
        }

        // 5.执行切点
        Throwable throwable = null;
        Object proceed = null;
        try {
            proceed = p.proceed();
            //拓展点,检测响应结果
            checkResult.checkResult(methodExecuteResult,proceed);
        } catch (Throwable e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage() == null ? "系统异常,请查看具体日志" : e.getMessage());
            debugProcessor.error("proceed exception", e);
            throwable = e;
        }
        // 10.执行记录
        try {
            if (atomicBoolean.get() && Objects.nonNull(operationLogDTO)) {
                recordExecute(proceed, getMethod(p), p.getArgs(),
                        operationLogDTO, getTargetClass(p.getTarget()), methodExecuteResult.isSuccess(),
                        methodExecuteResult.getErrorMsg(), functionNameAndReturnMap);
                //执行完记录后执行自定义方法,可以用来扩展
                afterRecordDoCustom(p);
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
     * @param ret
     * @param method
     * @param args
     * @param operation
     * @param targetClass
     * @param success
     * @param errorMsg
     * @param functionNameAndReturnMap
     */
    protected void recordExecute(Object ret, Method method, Object[] args, OperationLogDTO operation, Class<?> targetClass,
                                 boolean success, String errorMsg, Map<String, String> functionNameAndReturnMap) {

        String action = getActionContent(success, operation);
        // 获取需要解析的表达式
        List<String> spElTemplates = getSpElTemplates(operation, action);
        Map<String, String> expressionValues = processTemplate(spElTemplates, ret, targetClass, method, args, errorMsg, functionNameAndReturnMap);
        // condition is true
        if (logConditionPassed(operation.getCondition(), expressionValues)) {
            Operator operator = getOperator();
            String fileName = getFileName(args);
            operationLogService.insertLog(operation, expressionValues, action, operator, fileName, success);
        }

    }

    public String getFileName(Object[] args) {
        return null;
    }

    /**
     * 模板方法用于实现
     *
     * @param p
     */
    public void afterRecordDoCustom(ProceedingJoinPoint p) {

    }
}
