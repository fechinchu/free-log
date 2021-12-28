package cn.zhuguoqing.operationLog.service;

/**
 * @author guoqing.zhu
 *     <p>description:用于自定义函数用,直接实现该接口
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultParseFunction
 */
public interface IParseFunction {

  /**
   * 该方法是否在执行切点之前执行,默认为false;
   *
   * @return 返回是否在切点之前执行的boolean值;
   */
  default boolean executeBefore() {
    return false;
  }

  /**
   * 自定义方法
   *
   * @return 返回方法名称
   */
  String functionName();

  /**
   * 修正值自定义函数的值;
   *
   * @param value 修正前的值
   * @return 修正后的值
   */
  String apply(String value);
}
