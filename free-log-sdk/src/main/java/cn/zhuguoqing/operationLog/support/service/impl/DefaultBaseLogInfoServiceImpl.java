package cn.zhuguoqing.operationLog.support.service.impl;


import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.domain.OperationLogDomain;
import cn.zhuguoqing.operationLog.bean.dto.ImportFileDTO;
import cn.zhuguoqing.operationLog.support.service.IBaseLogInfoService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/3 13:48
 * @Desription: 默认实现
 * @Version: 1.0
 */

@Slf4j
public class DefaultBaseLogInfoServiceImpl implements IBaseLogInfoService {
    @Override
    public Long insertOperationLog(OperationLogDomain op) {
        log.info("DefaultBaseLogInfoServiceImpl.insertOperationLog:{}",op);
        return null;
    }

    @Override
    public void insertOperationLogDetail(List<OperationLogDetailDomain> opds) {
        log.info("DefaultBaseLogInfoServiceImpl.insertOperationLogDetail:{}",opds);
    }

    @Override
    public void importFile(ImportFileDTO importFileDTO) {
        log.info("DefaultBaseLogInfoServiceImpl.importFile:{}",importFileDTO);
    }
}
