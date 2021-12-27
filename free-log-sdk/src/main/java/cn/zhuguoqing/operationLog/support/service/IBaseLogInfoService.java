package cn.zhuguoqing.operationLog.support.service;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.domain.OperationLogDomain;
import cn.zhuguoqing.operationLog.bean.dto.ImportFileDTO;

import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/3 13:45
 * @Desription: TODO
 * @Version: 1.0
 */
public interface IBaseLogInfoService {

    /**
     * 插入主要日志
     * @param op
     * @return
     */
    Long insertOperationLog(OperationLogDomain op);

    /**
     * 插入详细日志
     * @param opds
     */
    void insertOperationLogDetail(List<OperationLogDetailDomain> opds);

    /**
     *
     * @param importFileDTO
     */
    void importFile(ImportFileDTO importFileDTO);

}
