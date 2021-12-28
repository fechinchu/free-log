package cn.zhuguoqing.operationLog.service;

import cn.zhuguoqing.operationLog.bean.dto.ColumnCommentDTO;
import cn.zhuguoqing.operationLog.bean.dto.SchemaTableNameDTO;

import java.util.List;
import java.util.Map;

/**
 * @author guoqing.zhu
 *     <p>description:查询数据库字段信息
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultOperationLogQueryServiceImpl
 */
public interface IOperationLogQueryService {

  /**
   * 查询任意表的数据
   *
   * @param sql
   * @return
   */
  Map<String, Object> selectAnyTable(String sql);

  /**
   * 查询任意表的数据获取List
   *
   * @param sql
   * @return
   */
  List<Map<String, Object>> selectAnyTableList(String sql);

  /**
   * 查询任意表的字段与备注
   *
   * @param dto 数据库的库名和表名
   * @return 表字段及其注释
   */
  List<ColumnCommentDTO> selectColumnCommentByTable(SchemaTableNameDTO dto);
}
