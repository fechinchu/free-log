package cn.zhuguoqing.demo.operationLog.Operator;

import cn.zhuguoqing.operationLog.bean.dto.Operator;
import cn.zhuguoqing.operationLog.service.IOperatorGetService;
import org.springframework.stereotype.Component;

/**
 * 2021/12/29 16:52
 *
 * @author guoqing.zhu
 *     <p>description:
 */
@Component
public class OperatorGet implements IOperatorGetService {
  @Override
  public Operator getUser() {
    //在这里根据自己项目,用户所在地方进行获取封装
    Operator operator = new Operator();
    operator.setOperatorId("10086");
    operator.setOperatorName("帅的朱");
    operator.setOperatorIp("192.168.155.155");
    return operator;
  }
}
