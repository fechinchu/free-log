package cn.zhuguoqing.operationLog.service;

/**
 * @author guoqing.zhu
 *     <p>description:注解中用于自定义函数用的自定义方法,该接口用于内部操作,实现自定义方法的外部操作:{@link IParseFunction}
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultFunctionServiceImpl
 */
public interface IFunctionService {

  /**
   * 执行IParseFunction的apply方法
   *
   * @param functionName 自定义方法名称
   * @param value 传入的值
   * @return 响应的值
   */
  String apply(String functionName, String value);

  /**
   * 是否在切点执行之前执行
   *
   * @param functionName 自定义方法名称
   * @return 是否在切点执行之前执行
   */
  boolean beforeFunction(String functionName);
}
