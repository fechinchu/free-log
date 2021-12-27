package cn.zhuguoqing.operationLog.support.service.impl;


import cn.zhuguoqing.operationLog.support.service.IFunctionService;
import cn.zhuguoqing.operationLog.support.service.IParseFunction;
import org.springframework.stereotype.Service;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/01
 * @Desription:默认实现
 * @Version: 1.0
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
