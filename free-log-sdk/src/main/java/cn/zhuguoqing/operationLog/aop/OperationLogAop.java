package cn.zhuguoqing.operationLog.aop;


import cn.zhuguoqing.operationLog.aop.strategy.OperationLogStrategyFactory;
import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author zhuguoqing
 * @date 2021-11-26
 */
@Aspect
@Component
@Slf4j
public class OperationLogAop {

    @Autowired
    private OperationLogStrategyFactory operationLogStrategyFactory;

    @Around(value = "@annotation(operationlog)")
    public Object logAround(final ProceedingJoinPoint p, final OperationLog operationlog) throws Throwable {

        return operationLogStrategyFactory.getStrategy(operationlog.type()).operate(p, operationlog);

    }
}
