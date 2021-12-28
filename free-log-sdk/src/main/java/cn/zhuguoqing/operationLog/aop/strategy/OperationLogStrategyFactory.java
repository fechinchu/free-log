package cn.zhuguoqing.operationLog.aop.strategy;

import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import cn.zhuguoqing.operationLog.support.debugger.DebugProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author guoqing.zhu
 *     <p>description:策略工厂
 */
@Component
@Slf4j
public class OperationLogStrategyFactory {

  @Autowired private List<OperationLogStrategy> strategies;

  @Autowired private DebugProcessor debugProcessor;

  public OperationLogStrategy getStrategy(OperationType type) {
    if (type == null) {
      debugProcessor.error("OperationLogStrategyFactory getStrategy cannot be null", null);
      return null;
    }
    Optional<OperationLogStrategy> strategy =
        strategies.stream().filter((s) -> type.equals(s.getOperationLogType())).findFirst();
    if (strategy.isPresent()) {
      return strategy.get();
    }
    debugProcessor.error("OperationLogStrategyFactory strategy cannot find", null);
    return null;
  }
}
