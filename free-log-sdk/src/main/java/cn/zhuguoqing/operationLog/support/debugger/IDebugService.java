package cn.zhuguoqing.operationLog.support.debugger;


import cn.zhuguoqing.operationLog.bean.dto.OperationDebugDTO;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/14 16:53
 * @Desription: TODO
 * @Version: 1.0
 */
public interface IDebugService {

    /**
     * 记录错误信息
     * @param operationDebugDTO
     * @return
     */
    Integer saveErrorInfo(OperationDebugDTO operationDebugDTO);
}
