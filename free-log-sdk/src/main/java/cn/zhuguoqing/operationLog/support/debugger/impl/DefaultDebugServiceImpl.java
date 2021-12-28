package cn.zhuguoqing.operationLog.support.debugger.impl;

import cn.zhuguoqing.operationLog.bean.dto.OperationDebugDTO;
import cn.zhuguoqing.operationLog.support.debugger.IDebugService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author guoqing.zhu
 *     <p>description:默认的用于Debug的错误信息记录
 */
@Slf4j
public class DefaultDebugServiceImpl implements IDebugService {

  @Override
  public void saveErrorInfo(OperationDebugDTO operationDebugDTO) {
    log.error("saveErrorInfo:dto:{}", operationDebugDTO);
  }
}
