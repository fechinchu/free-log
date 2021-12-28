package cn.zhuguoqing.operationLog.support.debugger;

import cn.zhuguoqing.operationLog.bean.dto.OperationDebugDTO;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import cn.zhuguoqing.operationLog.support.util.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author guoqing.zhu
 *     <p>description:用于Debug的processor.
 */
@Component
@Slf4j
public class DebugProcessor {

  @Autowired private IDebugService debugService;

  public void error(String errorMessage, Throwable t) {
    long id = SnowFlakeUtil.getId();
    log.error(errorMessage + " id = " + id, t);
    Object logIdObj = LogRecordContext.getVariable("logId");
    Long logId = null;
    if (logIdObj != null) {
      logId = Long.parseLong(logIdObj.toString());
    }
    OperationDebugDTO dto =
        new OperationDebugDTO(
            logId, id, t == null ? null : t.getMessage() == null ? t.toString() : t.getMessage());
    debugService.saveErrorInfo(dto);
  }
}
