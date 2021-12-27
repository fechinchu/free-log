package cn.zhuguoqing.operationLog.support.service;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/01
 * @Desription:
 * @Version: 1.0
 */
public interface IFunctionService {

    /**
     * 执行IParseFunction的apply方法
     * @param functionName
     * @param value
     * @return
     */
    String apply(String functionName, String value);

    /**
     * 是否在procceed之前执行
     * @param functionName
     * @return
     */
    boolean beforeFunction(String functionName);
}
