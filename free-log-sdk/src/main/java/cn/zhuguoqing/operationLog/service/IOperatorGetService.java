package cn.zhuguoqing.operationLog.service;

import cn.zhuguoqing.operationLog.bean.dto.Operator;

/**
 * @author guoqing.zhu
 *     <p>description:实现该接口,来将当前用户保存在日志中
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultOperatorGetServiceImpl
 */
public interface IOperatorGetService {

  /**
   * 可以在里面获取当前登陆的用户; 一般项目中会将当前用户信息放入在ThreadLocal. ThreadLocal.getUserId(),ThreadLocal.getUserName();
   *
   * @return 转换成Operator返回
   */
  Operator getUser();
}
