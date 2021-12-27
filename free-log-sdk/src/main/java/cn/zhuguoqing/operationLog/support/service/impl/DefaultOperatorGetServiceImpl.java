package cn.zhuguoqing.operationLog.support.service.impl;



import cn.zhuguoqing.operationLog.bean.dto.Operator;
import cn.zhuguoqing.operationLog.support.service.IOperatorGetService;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/01
 * @Desription:
 * @Version: 1.0
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
