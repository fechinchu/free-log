package cn.zhuguoqing.operationLog.aop.strategy;

import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author guoqing.zhu
 *     <p>description:新增数据的策略
 */
@Component
@Slf4j
public class AddStrategy extends AbstractBaseStrategyTemplate {

  @Override
  public OperationType getOperationLogType() {
    return OperationType.ADD;
  }
}
