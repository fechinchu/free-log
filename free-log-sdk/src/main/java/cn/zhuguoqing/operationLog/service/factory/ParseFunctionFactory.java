package cn.zhuguoqing.operationLog.service.factory;

import cn.zhuguoqing.operationLog.service.IParseFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guoqing.zhu
 *     <p>description:用于存放所有注解中自定义函数的工厂类
 * @see IParseFunction
 */
@Component
public class ParseFunctionFactory {

  private Map<String, IParseFunction> allFunctionMap;

  @Autowired
  public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
    if (CollectionUtils.isEmpty(parseFunctions)) {
      return;
    }
    allFunctionMap = new ConcurrentHashMap<>();
    for (IParseFunction parseFunction : parseFunctions) {
      if (StringUtils.isEmpty(parseFunction.functionName())) {
        continue;
      }
      allFunctionMap.put(parseFunction.functionName(), parseFunction);
    }
  }

  /**
   * 根据方法名称获取方法;
   *
   * @param functionName 方法名;
   * @return 方法的实体
   */
  public IParseFunction getFunction(String functionName) {
    return allFunctionMap.get(functionName);
  }

  /**
   * 根据方法名查询该方法是否在切点执行之前执行;
   *
   * @param functionName 方法名;
   * @return 是否在切点执行之前执行
   */
  public boolean isBeforeFunction(String functionName) {
    return allFunctionMap.get(functionName) != null
        && allFunctionMap.get(functionName).executeBefore();
  }
}
