package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.aop.strategy.AbstractOperationLogStrategy;
import cn.zhuguoqing.operationLog.service.ICheckResultService;

/**
 * @author guoqing.zhu
 *     <p>description:默认实现
 */
public class DefaultCheckResultServiceImpl implements ICheckResultService {

  @Override
  public void checkResult(
      AbstractOperationLogStrategy.MethodExecuteResult methodExecuteResult, Object proceed) {}
}
