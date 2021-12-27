package cn.zhuguoqing.operationLog.aop.strategy;

import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import org.springframework.stereotype.Component;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/3 15:55
 * @Desription: TODO
 * @Version: 1.0
 */
@Component
public class UpdateStrategy extends AbstractBaseStrategyTemplate {

    @Override
    public OperationType getOperationLogType() {
        return OperationType.UPDATE;
    }
}
