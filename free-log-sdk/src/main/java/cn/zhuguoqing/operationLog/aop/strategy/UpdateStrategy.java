package cn.zhuguoqing.operationLog.aop.strategy;

import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import org.springframework.stereotype.Component;

/**
 * @author guoqing.zhu
 *     <p>description:简单修改的策略,这里不记录旧值新值
 */
@Component
public class UpdateStrategy extends AbstractBaseStrategyTemplate {

  @Override
  public OperationType getOperationLogType() {
    return OperationType.UPDATE;
  }
}
