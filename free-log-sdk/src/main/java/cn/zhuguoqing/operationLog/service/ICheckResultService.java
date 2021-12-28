package cn.zhuguoqing.operationLog.service;

import cn.zhuguoqing.operationLog.aop.strategy.AbstractOperationLogStrategy;

/**
 * 2021/12/28 15:43
 *
 * @author guoqing.zhu
 *     <p>description:该类用于适配代码中封装的返回结果;
 *     <p>在一些项目中:在控制层没有抛出异常,而是对结果进行封装;这时候实现该类,可以对异常或正常结果进行记录
 *     <p>如果所在项目中并没有封装结果,可以不用实现该接口,系统会自己直接使用默认的
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultCheckResultServiceImpl
 */
public interface ICheckResultService {

  /**
   * @param methodExecuteResult 需要进行封装的结果集;
   * @param proceed 切点执行后的结果
   */
  void checkResult(
      AbstractOperationLogStrategy.MethodExecuteResult methodExecuteResult, Object proceed);
}
