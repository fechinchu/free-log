package cn.zhuguoqing.operationLog.support.diff;

import cn.zhuguoqing.operationLog.bean.enums.DiffType;
import cn.zhuguoqing.operationLog.support.debugger.DebugProcessor;
import com.google.common.annotations.Beta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author guoqing.zhu
 *     <p>description:DiffProcessor策略工厂
 */
@Component
@Beta
public class DiffProcessorFactory {

  @Autowired private DebugProcessor debugProcessor;

  @Autowired private List<IDiffProcessor> diffProcessors;

  /**
   * 根据Diff策略获取DiffProcessor
   *
   * @param type 策略类型
   * @return DiffProcessor
   */
  public IDiffProcessor getDiffProcessor(DiffType type) {
    if (type == null) {
      debugProcessor.error("DiffProcessorFactory type cannot be null", null);
      return null;
    }
    Optional<IDiffProcessor> processor =
        diffProcessors.stream().filter((s) -> type.equals(s.getDiffType())).findFirst();
    if (processor.isPresent()) {
      return processor.get();
    }
    debugProcessor.error("DiffProcessorFactory strategy cannot find", null);
    return null;
  }
}
