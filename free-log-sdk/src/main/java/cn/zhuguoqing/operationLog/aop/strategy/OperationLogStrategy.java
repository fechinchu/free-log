package cn.zhuguoqing.operationLog.aop.strategy;

import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author guoqing.zhu
 *     <p>description:由于log日志记录分增删改查以及上传以及批量导入,批量修改等操作,操作内容种类比较多,可以使用策略模式,如下就是策略接口
 *     <p>注意: 后期如果需要新增需要记录日志的操作,可以实现该接口,也可以继承 {@link AbstractOperationLogStrategy}或者{@link
 *     AbstractBaseStrategyTemplate}
 */
public interface OperationLogStrategy {

  /**
   * 增删改的抽象方法
   *
   * @param p 切点
   * @param operationlog 自定义的注解
   * @return
   */
  Object operate(final ProceedingJoinPoint p, final OperationLog operationlog);

  /**
   * 获取策略类型
   *
   * @return
   */
  OperationType getOperationLogType();
}
