package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.domain.OperationLogDomain;
import cn.zhuguoqing.operationLog.bean.dto.ImportFileDTO;
import cn.zhuguoqing.operationLog.service.ILogInsertAndImportService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author guoqing.zhu
 *     <p>description:默认实现
 * @see AbstractLogInsertAndImportService
 * @see ILogInsertAndImportService
 */
@Slf4j
public class DefaultLogInsertAndImportServiceImpl implements ILogInsertAndImportService {

  @Override
  public void insertOperationLog(OperationLogDomain op) {
    log.info("DefaultLogInsertAndImportServiceImpl.insertOperationLog,param:{}", op);
  }

  @Override
  public void insertOperationLogDetail(List<OperationLogDetailDomain> opds) {
    log.info("DefaultLogInsertAndImportServiceImpl.insertOperationLogDetail,param:{}", opds);
  }

  @Override
  public void importFile(ImportFileDTO importFileDTO) {
    log.info("DefaultLogInsertAndImportServiceImpl.importFile,param:{}", importFileDTO);
  }
}
