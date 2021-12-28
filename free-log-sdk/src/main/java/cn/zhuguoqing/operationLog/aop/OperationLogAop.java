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
 * @author guoqing.zhu
 *     <p>description:日志切面
 */
@Aspect
@Component
@Slf4j
public class OperationLogAop {

  @Autowired private OperationLogStrategyFactory operationLogStrategyFactory;

  /**
   * 切面
   *
   * @param p 切点
   * @param operationlog operationlog注解
   * @return 返回切点执行后的结果
   */
  @Around(value = "@annotation(operationlog)")
  public Object logAround(final ProceedingJoinPoint p, final OperationLog operationlog) {

    return operationLogStrategyFactory.getStrategy(operationlog.type()).operate(p, operationlog);
  }
}
