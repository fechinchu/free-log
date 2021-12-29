package cn.zhuguoqing.operationLog.bean.enums;

/**
 * @author guoqing.zhu
 *     <p>description:日志的策略类型枚举
 */
public enum OperationType {

  /** 添加 */
  ADD,
  /** 简单修改 */
  UPDATE,
  /** 复杂修改 */
  COMPLEX_UPDATE,
  /** 删除 */
  DELETE,
  /** 导入 */
  IMPORT,
  /** 简单操作 */
  SIMPLE;

  public String getType() {
    if (this.equals(ADD)) {
      return "ADD";
    }
    if (this.equals(UPDATE)) {
      return "UPDATE";
    }
    if (this.equals(DELETE)) {
      return "DELETE";
    }
    if (this.equals(IMPORT)) {
      return "IMPORT";
    }
    if (this.equals(COMPLEX_UPDATE)) {
      return "COMPLEX_UPDATE";
    }
    if(this.equals(SIMPLE)){
      return "SIMPLE";
    }
    return null;
  }
}
