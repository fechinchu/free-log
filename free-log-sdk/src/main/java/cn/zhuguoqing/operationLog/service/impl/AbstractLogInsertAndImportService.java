package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.domain.OperationLogDomain;
import cn.zhuguoqing.operationLog.bean.dto.ImportFileDTO;
import cn.zhuguoqing.operationLog.configuration.OperationProperties;
import cn.zhuguoqing.operationLog.mapper.OperationLogMapper;
import cn.zhuguoqing.operationLog.service.ILogInsertAndImportService;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author guoqing.zhu
 *     <p>description:日志插入和文件导入的抽象类,可以继承该抽象类来实现自己的日志插入和文件导入的功能
 */
public abstract class AbstractLogInsertAndImportService implements ILogInsertAndImportService {

  @Autowired public OperationProperties operationProperties;

  @Autowired public OperationLogMapper operationLogMapper;

  @Override
  public void importFile(ImportFileDTO importFileDTO) {
    String originalFilename = importFileDTO.getFileName();
    byte[] bytes = importFileDTO.getBytes();
    /*-----将文件上传到服务器-----*/
    FileOutputStream out = null;
    // 对文件名称进行切割
    String lastName = originalFilename.substring(originalFilename.lastIndexOf("."));
    try {
      String fname =
          new String(operationProperties.getImportUrl() + importFileDTO.getFileId() + lastName);
      File file = new File(fname);
      // 如果路径不存在,创建路径
      File fileParent = file.getParentFile();
      if (!fileParent.exists()) {
        fileParent.mkdirs();
      }
      file.createNewFile();
      out = new FileOutputStream(file);
      Files.write(bytes, file);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void insertOperationLog(OperationLogDomain op) {
    operationLogMapper.insertOperationLog(op);
  }

  @Override
  public void insertOperationLogDetail(List<OperationLogDetailDomain> opds) {
    operationLogMapper.insertOperationLogDetail(opds);
  }
}
