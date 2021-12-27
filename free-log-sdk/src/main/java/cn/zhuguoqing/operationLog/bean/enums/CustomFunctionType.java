package cn.zhuguoqing.operationLog.bean.enums;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/16 00:18
 * @Desription: TODO
 * @Version: 1.0
 */
public enum CustomFunctionType {

    KEY,
    VALUE;

    public String getType() {
        if (this.equals(KEY)) {
            return "KEY";
        }
        if (this.equals(VALUE)) {
            return "VALUE";
        }
        return null;
    };
}
