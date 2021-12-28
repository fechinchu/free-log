package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author guoqing.zhu
 *     <p>description:导入文件用的实体封装
 */
@Data
@ToString
public class ImportFileDTO {

  /** 文件id */
  private Long fileId;

  /** 文件名称 */
  private String fileName;

  /** 字节数组 */
  private byte[] bytes;
}
