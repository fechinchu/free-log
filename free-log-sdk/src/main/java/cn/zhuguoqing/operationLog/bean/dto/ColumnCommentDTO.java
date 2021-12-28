package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;

/**
 * @author guoqing.zhu
 *     <p>description:封装表字段和表字段的注释
 */
@Data
public class ColumnCommentDTO {

  /** 表字段 */
  private String column;

  /** 表字段注释 */
  private String comment;
}
