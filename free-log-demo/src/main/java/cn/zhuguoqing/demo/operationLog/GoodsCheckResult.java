package cn.zhuguoqing.demo.operationLog;

import cn.zhuguoqing.demo.common.model.BasicResult;
import cn.zhuguoqing.operationLog.aop.strategy.AbstractOperationLogStrategy;
import cn.zhuguoqing.operationLog.service.ICheckResultService;
import org.springframework.stereotype.Component;

/**
 * 2021/12/28 16:06
 *
 * <p>description:该类用于适配代码中封装的返回结果;
 *
 * <p>一些项目中:在控制层没有抛出异常,而是对结果进行封装;这时候实现该类,可以对异常或正常结果进行记录
 *
 * @author guoqing.zhu
 */
@Component
public class GoodsCheckResult implements ICheckResultService {
  @Override
  public void checkResult(
      AbstractOperationLogStrategy.MethodExecuteResult methodExecuteResult, Object proceed) {

    if (proceed != null) {
      if (proceed instanceof BasicResult) {
        BasicResult result = (BasicResult) proceed;
        if (!result.isSuccess()) {
          String message = result.getMessage();
          methodExecuteResult.setErrorMsg(message);
          methodExecuteResult.setSuccess(false);
        }
      }
    }
  }
}
