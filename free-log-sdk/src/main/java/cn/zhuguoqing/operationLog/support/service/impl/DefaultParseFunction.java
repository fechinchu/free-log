package cn.zhuguoqing.operationLog.support.service.impl;

import cn.zhuguoqing.operationLog.support.service.IParseFunction;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/01
 * @Desription: IParseFunction的默认实现
 * @Version: 1.0
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
