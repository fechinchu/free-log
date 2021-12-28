package cn.zhuguoqing.operationLog.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author guoqing.zhu
 *     <p>description:用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operator {

  /** 操作人ID */
  private String operatorId;

  /** 操作人姓名 */
  private String operatorName;

  /** 操作人IP */
  private String operatorIp;
}
