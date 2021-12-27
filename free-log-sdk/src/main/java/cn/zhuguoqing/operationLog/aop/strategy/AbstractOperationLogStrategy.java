package cn.zhuguoqing.operationLog.aop.strategy;


import cn.zhuguoqing.operationLog.bean.dto.OperationLogDTO;
import cn.zhuguoqing.operationLog.bean.dto.Operator;
import cn.zhuguoqing.operationLog.support.debugger.DebugProcessor;
import cn.zhuguoqing.operationLog.support.debugger.IDebugService;
import cn.zhuguoqing.operationLog.support.parser.LogRecordOperationSourceParser;
import cn.zhuguoqing.operationLog.support.parser.LogRecordValueParser;
import cn.zhuguoqing.operationLog.support.service.ICheckResult;
import cn.zhuguoqing.operationLog.support.service.IOperationLogQueryService;
import cn.zhuguoqing.operationLog.support.service.IOperatorGetService;
import cn.zhuguoqing.operationLog.support.service.impl.OperationLogsService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author:guoqing.zhu
 * @Date：2021/11/26 15:29
 * @Desription: TODO
 * @Version: 1.0
 */
@Slf4j
public abstract class AbstractOperationLogStrategy extends LogRecordValueParser implements OperationLogStrategy {

    @Autowired
    protected IDebugService iDebugService;

    @Autowired
    protected IOperationLogQueryService iOperationLogService;

    @Autowired
    protected OperationLogsService operationLogService;

    @Autowired
    protected LogRecordOperationSourceParser logRecordOperationSourceParser;

    @Autowired
    protected IOperatorGetService iOperatorGetService;

    @Autowired
    protected IDebugService debugService;

    @Autowired
    protected DebugProcessor debugProcessor;

    @Autowired
    protected ICheckResult checkResult;


    protected boolean logConditionPassed(String condition, Map<String, String> expressionValues) {
        return StringUtils.isEmpty(condition) || StringUtils.endsWithIgnoreCase(expressionValues.get(condition), "true");
    }

    protected String getActionContent(boolean success, OperationLogDTO operation) {
        String action = "";
        if (success) {
            action = operation.getSuccess();
        } else {
            action = operation.getFail();
        }
        return action;
    }

    protected String[] getColumIfColumAnnoIsNull(Map<String, Object> columnCommentMap, String[] cloum, String[] excludeColumn) {
        if (cloum.length == 0) {
            Set<String> keySet = columnCommentMap.keySet();
            List<String> list = new ArrayList<>(keySet);
            if (excludeColumn.length != 0) {
                list.removeAll(new ArrayList<>(Arrays.asList(excludeColumn)));
            }
            cloum = list.toArray(new String[0]);
        }
        return cloum;
    }

    protected List<String> getProcessBeforeExcuteTemplate(OperationLogDTO operation) {
        List<String> spElTemplates = Lists.newArrayList();
        if (!StringUtils.isEmpty(operation.getIdRef())) {
            spElTemplates.add(operation.getIdRef());
        }
        return spElTemplates;
    }

    protected List<String> getBeforeExecuteFunctionTemplate(OperationLogDTO operation) {
        List<String> spElTemplates = new ArrayList<>();
        // 执行之前的函数，失败模版不解析
        List<String> templates = getSpElTemplates(operation, operation.getSuccess());
        if (!CollectionUtils.isEmpty(templates)) {
            spElTemplates.addAll(templates);
        }
        return spElTemplates;
    }

    /**
     * 获取IdRef+detail+customMethod+condition
     *
     * @param operation
     * @param action
     * @return
     */
    protected List<String> getSpElTemplates(OperationLogDTO operation, String action) {
        List<String> spElTemplates = Lists.newArrayList(action);
        if (!StringUtils.isEmpty(operation.getCondition())) {
            spElTemplates.add(operation.getCondition());
        }
        if (!StringUtils.isEmpty(operation.getIdRef())) {
            spElTemplates.add(operation.getIdRef());
        }
        if (!StringUtils.isEmpty(operation.getDetail())) {
            spElTemplates.add(operation.getDetail());
        }
        if (!StringUtils.isEmpty(operation.getCustomMethod())) {
            spElTemplates.add(operation.getCustomMethod());
        }

        return spElTemplates;
    }

    protected Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }

    @SneakyThrows
    protected Method getMethod(ProceedingJoinPoint p) {
        Signature sig = p.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = p.getTarget();
        return target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
    }

    /**
     * 获取Operator
     *
     * @return
     */
    protected Operator getOperator() {
        Operator operator = iOperatorGetService.getUser();
        if (Objects.isNull(operator)) {
            throw new IllegalArgumentException("[LogRecord] operator is null");
        }
        return operator;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MethodExecuteResult {
        private boolean success;
        private Throwable throwable;
        private String errorMsg;
    }

}
