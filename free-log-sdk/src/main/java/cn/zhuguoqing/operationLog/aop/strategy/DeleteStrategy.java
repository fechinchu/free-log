package cn.zhuguoqing.operationLog.aop.strategy;


import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author:guoqing.zhu
 * @Date：2021/11/26 15:45
 * @Desription: TODO
 * @Version: 1.0
 */
@Component
@Slf4j
public class DeleteStrategy extends AbstractBaseStrategyTemplate {

    @Override
    public OperationType getOperationLogType() {
        return OperationType.DELETE;
    }
}
