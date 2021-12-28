package cn.zhuguoqing.operationLog.support.debugger.impl;

import cn.zhuguoqing.operationLog.bean.dto.OperationDebugDTO;
import cn.zhuguoqing.operationLog.mapper.OperationLogMapper;
import cn.zhuguoqing.operationLog.support.debugger.IDebugService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 2021/12/29 00:05
 *
 * @author guoqing.zhu
 *     <p>description:
 */
public abstract class AbstractDebugService implements IDebugService {

    @Autowired
    public OperationLogMapper operationLogMapper;


    @Override
    public void saveErrorInfo(OperationDebugDTO operationDebugDTO) {
        operationLogMapper.saveErrorInfo(operationDebugDTO);
    }
}
