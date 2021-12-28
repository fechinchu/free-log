package cn.zhuguoqing.operationLog.mapper;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.domain.OperationLogDomain;
import cn.zhuguoqing.operationLog.bean.dto.ColumnCommentDTO;
import cn.zhuguoqing.operationLog.bean.dto.OperationDebugDTO;
import cn.zhuguoqing.operationLog.bean.dto.SchemaTableNameDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 2021/12/28 17:49
 *
 * @author guoqing.zhu
 *     <p>description:日志的Mapper层
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultOperationLogQueryServiceImpl
 * @see cn.zhuguoqing.operationLog.service.impl.AbstractLogInsertAndImportService
 */
@Mapper
public interface OperationLogMapper {

  /** 查询任意表获取数据 */
  @SelectProvider(type = OperationLogMapperProvider.class, method = "selectAnyTableSql")
  Map<String, Object> selectAnyTable(@Param("sql") String sql);

  /**
   * 查询任意表获取List
   *
   * @param sql
   * @return
   */
  @SelectProvider(type = OperationLogMapperProvider.class, method = "selectAnyTableSql")
  List<Map<String, Object>> selectAnyTableList(@Param("sql") String sql);

  /** 查询任意表的字段与备注 */
  @Select(
      "SELECT COLUMN_NAME `column`,column_comment `comment` FROM INFORMATION_SCHEMA.Columns "
          + "WHERE table_schema=#{s.tableSchema} and table_name=#{s.tableName}")
  List<ColumnCommentDTO> selectColumnCommentByTable(@Param("s") SchemaTableNameDTO dto);

  /**
   * 插入日志主表
   *
   * @param data
   */
  @Insert(
      "INSERT INTO operation_log (id,name,table_name,table_id,type,action,detail,operator_id,"
          + "operator_name,operator_ip,operation_time,import_filename,if_success) "
          + "VALUES (#{p.id},#{p.name},#{p.tableName},#{p.tableId},#{p.type},#{p.action},"
          + "#{p.detail},#{p.operatorId},#{p.operatorName},#{p.operatorIp},#{p.operationTime},"
          + "#{p.importFilename},#{p.ifSuccess});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "p.id")
  void insertOperationLog(@Param("p") OperationLogDomain data);

  /**
   * 插入日志详情表
   *
   * @param data
   */
  @InsertProvider(
      type = OperationLogDetailMapperProvider.class,
      method = "insertOperationLogDetailSql")
  void insertOperationLogDetail(@Param("ops") List<OperationLogDetailDomain> data);

  @Insert(
      "insert into operation_log_debug (operation_log_id,error_info,log_positioning_id,create_time)"
          + " values (#{operationLogId},#{errorInfo},#{logPositioningId},sysdate()")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "p.id")
  void saveErrorInfo(OperationDebugDTO operationDebugDTO);

  class OperationLogMapperProvider {
    public String selectAnyTableSql(Map<String, String> map) {
      return map.get("sql");
    }
  }

  class OperationLogDetailMapperProvider {
    public String insertOperationLogDetailSql(Map<String, List<OperationLogDetailDomain>> map) {
      List<OperationLogDetailDomain> ops = map.get("ops");
      StringBuilder sqlBuid =
          new StringBuilder(
              "INSERT INTO operation_log_detail (operation_log_id,clm_name,clm_comment,old_string,new_string) VALUES ");
      for (int i = 0; i < ops.size(); i++) {
        OperationLogDetailDomain o = ops.get(i);
        if (i == 0) {
          sqlBuid.append(
              " ('"
                  + o.getOperationLogId()
                  + "','"
                  + o.getClmName()
                  + "','"
                  + o.getClmComment()
                  + "','"
                  + o.getOldString()
                  + "','"
                  + o.getNewString()
                  + "') ");
        } else {
          sqlBuid.append(
              " ,('"
                  + o.getOperationLogId()
                  + "','"
                  + o.getClmName()
                  + "','"
                  + o.getClmComment()
                  + "','"
                  + o.getOldString()
                  + "','"
                  + o.getNewString()
                  + "') ");
        }
      }
      return sqlBuid.toString();
    }
  }
}
