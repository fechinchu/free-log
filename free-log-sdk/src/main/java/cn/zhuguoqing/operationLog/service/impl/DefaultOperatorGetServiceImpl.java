package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.bean.dto.Operator;
import cn.zhuguoqing.operationLog.service.IOperatorGetService;

/**
 * @author guoqing.zhu
 *     <p>description:默认实现
 */
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

  @Override
  public Operator getUser() {
    Operator operator = new Operator();
    operator.setOperatorId("");
    operator.setOperatorName("");
    operator.setOperatorIp("");
    return operator;
  }
}
