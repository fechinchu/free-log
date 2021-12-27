package cn.zhuguoqing.operationLog.support.service;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/01
 * @Desription:
 * @Version: 1.0
 */
public interface IParseFunction {

    /**
     * 该方法是否在procceed执行之前执行
     * @return
     */
    default boolean executeBefore(){
        return true;
    }

    /**
     * 方法名称
     * @return
     */
    String functionName();

    /**
     * 修正值和自定义函数用
     * @param value
     * @return
     */
    String apply(String value);
}
