package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author guoqing.zhu
 *     <p>description:封装schemaName和tableName的实体
 */
@Data
@ToString
public class SchemaTableNameDTO {

  /** 库名 */
  private String schemaName;

  /** 表名 */
  private String tableName;
}
