package cn.zhuguoqing.operationLog.bean.enums;

import cn.zhuguoqing.operationLog.service.factory.ModifyColCommentValueFactory;

/**
 * @author guoqing.zhu
 *     <p>description:全局自定义方法的是用来修改字段的注释的还是用来修改字段的值的
 * @see ModifyColCommentValueFactory
 */
public enum CustomFunctionType {
  /** 注释 */
  KEY,
  /** 值 */
  VALUE;

  public String getType() {
    if (this.equals(KEY)) {
      return "KEY";
    }
    if (this.equals(VALUE)) {
      return "VALUE";
    }
    return null;
  }
  ;
}
