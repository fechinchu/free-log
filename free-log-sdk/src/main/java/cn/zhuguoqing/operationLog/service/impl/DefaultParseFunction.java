package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.service.IParseFunction;

/**
 * @author guoqing.zhu
 *     <p>description:默认实现
 */
public class DefaultParseFunction implements IParseFunction {

  @Override
  public boolean executeBefore() {
    return true;
  }

  @Override
  public String functionName() {
    return null;
  }

  @Override
  public String apply(String value) {
    return null;
  }
}
