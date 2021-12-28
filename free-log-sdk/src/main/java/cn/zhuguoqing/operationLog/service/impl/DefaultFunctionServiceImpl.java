package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.service.IFunctionService;
import cn.zhuguoqing.operationLog.service.IParseFunction;
import cn.zhuguoqing.operationLog.service.factory.ParseFunctionFactory;
import org.springframework.stereotype.Service;

/**
 * @author guoqing.zhu
 *     <p>description:默认实现
 */
@Service
public class DefaultFunctionServiceImpl implements IFunctionService {

  private final ParseFunctionFactory parseFunctionFactory;

  public DefaultFunctionServiceImpl(ParseFunctionFactory parseFunctionFactory) {
    this.parseFunctionFactory = parseFunctionFactory;
  }

  @Override
  public String apply(String functionName, String value) {
    IParseFunction function = parseFunctionFactory.getFunction(functionName);
    if (function == null) {
      return value;
    }
    return function.apply(value);
  }

  @Override
  public boolean beforeFunction(String functionName) {
    return parseFunctionFactory.isBeforeFunction(functionName);
  }
}
