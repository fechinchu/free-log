package cn.zhuguoqing.operationLog.support.debugger;

import cn.zhuguoqing.operationLog.bean.dto.OperationDebugDTO;

/**
 * @author guoqing.zhu
 *     <p>description:用于记录错误日志的debugger接口
 */
public interface IDebugService {

  /**
   * 记录错误信息
   *
   * @param operationDebugDTO 封装的错误信息
   */
  void saveErrorInfo(OperationDebugDTO operationDebugDTO);
}
