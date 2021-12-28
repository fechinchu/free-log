package cn.zhuguoqing.operationLog.bean.enums;

/**
 * @author guoqing.zhu
 *     <p>description:Diff类的策略类型
 * @see cn.zhuguoqing.operationLog.support.diff.DiffProcessorFactory
 */
public enum DiffType {
  /** 单条数据更新 */
  SINGLE_UPDATE,
  /** 列表的增删改; */
  LIST_ADD_DELETE,
  /** 单条数据添加 */
  SINGLE_ADD;

  public String getType() {
    if (this.equals(SINGLE_UPDATE)) {
      return "SINGLE_UPDATE";
    }
    if (this.equals(LIST_ADD_DELETE)) {
      return "LIST_ADD_DELETE";
    }
    if (this.equals(SINGLE_ADD)) {
      return "SINGLE_ADD";
    }
    return null;
  }
  ;
}
