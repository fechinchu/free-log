package cn.zhuguoqing.operationLog.support.service;


import cn.zhuguoqing.operationLog.bean.dto.Operator;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/01
 * @Desription:
 * @Version: 1.0
 */
public interface IOperatorGetService {

    /**
     * 可以在里面外部的获取当前登陆的用户，比如SystemContext.getUserId(),比如SystemContext.getUserName();
     *
     * @return 转换成Operator返回
     */
    Operator getUser();
}
