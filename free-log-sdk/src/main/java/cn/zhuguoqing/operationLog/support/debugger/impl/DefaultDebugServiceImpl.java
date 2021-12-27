package cn.zhuguoqing.operationLog.support.debugger.impl;


import cn.zhuguoqing.operationLog.bean.dto.OperationDebugDTO;
import cn.zhuguoqing.operationLog.support.debugger.IDebugService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/14 17:01
 * @Desription: TODO
 * @Version: 1.0
 */
@Slf4j
public class DefaultDebugServiceImpl implements IDebugService {

    @Override
    public Integer saveErrorInfo(OperationDebugDTO operationDebugDTO) {
        log.error("saveErrorInfo:dto:{}",operationDebugDTO);
        return -1;
    }
}
