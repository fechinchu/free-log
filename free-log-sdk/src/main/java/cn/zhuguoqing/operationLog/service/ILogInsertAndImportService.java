package cn.zhuguoqing.operationLog.service;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.domain.OperationLogDomain;
import cn.zhuguoqing.operationLog.bean.dto.ImportFileDTO;

import java.util.List;

/**
 * @author guoqing.zhu
 *     <p>description:告诉free-log日志该记录在什么地方,以及文件存放的位置
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultLogInsertAndImportServiceImpl
 */
public interface ILogInsertAndImportService {

  /**
   * 插入主要日志
   *
   * @param op 主要日志
   * @return
   */
  void insertOperationLog(OperationLogDomain op);

  /**
   * 插入详细日志
   *
   * @param opds 详细日志集合
   */
  void insertOperationLogDetail(List<OperationLogDetailDomain> opds);

  /**
   * 插入文件 如果项目中有需要导入的功能,可以实现,如果没有就不用实现
   *
   * @param importFileDTO 文件信息,包括文件名称,文件id,以及文件的byte数组
   * 文件id主要用于区别各个文件,用的是日志主表的主键;
   */
  void importFile(ImportFileDTO importFileDTO);
}
