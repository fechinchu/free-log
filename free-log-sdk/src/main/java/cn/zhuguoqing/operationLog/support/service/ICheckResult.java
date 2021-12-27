package cn.zhuguoqing.operationLog.support.service;


import cn.zhuguoqing.operationLog.aop.strategy.AbstractOperationLogStrategy;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/23 16:04
 * @Desription: TODO
 * @Version: 1.0
 */
public interface ICheckResult {

    void checkResult(AbstractOperationLogStrategy.MethodExecuteResult methodExecuteResult, Object proceed);
}
