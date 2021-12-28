package cn.zhuguoqing.operationLog.service;

import cn.zhuguoqing.operationLog.bean.enums.CustomFunctionType;

import java.util.List;

/**
 * @author guoqing.zhu
 *     <p>description:全局用更改某个字段的注释,或者更改某个字段值用,实现该接口用于自定义;
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultModifyColCommentValueServiceImpl
 */
public interface IModifyColCommentValueService {

  /**
   * 获取类型
   *
   * @return 类型
   */
  CustomFunctionType getType();

  /**
   * 获取名称
   *
   * @return 字段的名称 example:schemaName.tableName.colName;
   */
  List<String> getName();

  /**
   * 转换
   *
   * @param param 转换前的值
   * @return 转换后的值
   */
  String modify(String param);
}
