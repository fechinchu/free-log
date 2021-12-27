package cn.zhuguoqing.operationLog.bean.enums;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/9 16:36
 * @Desription: TODO
 * @Version: 1.0
 */
public enum DiffType {
    SINGLE_UPDATE,
    LIST_ADD_DELETE,
    SINGLE_ADD,
    LIST_UPDATE;

    public String getType() {
        if (this.equals(SINGLE_UPDATE)) {
            return "SINGLE_UPDATE";
        }
        if (this.equals(LIST_ADD_DELETE)) {
            return "LIST_ADD_DELETE";
        }
        if(this.equals(SINGLE_ADD)){
            return "SINGLE_ADD";
        }
        if(this.equals(LIST_UPDATE)){
            return "LIST_UPDATE";
        }
        return null;
    };
}
