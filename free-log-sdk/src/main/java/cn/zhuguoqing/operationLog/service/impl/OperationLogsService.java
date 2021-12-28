package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.domain.OperationLogDomain;
import cn.zhuguoqing.operationLog.bean.dto.*;
import cn.zhuguoqing.operationLog.bean.enums.CustomFunctionType;
import cn.zhuguoqing.operationLog.service.IModifyColCommentValueService;
import cn.zhuguoqing.operationLog.service.IOperationLogQueryService;
import cn.zhuguoqing.operationLog.service.factory.ModifyColCommentValueFactory;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import cn.zhuguoqing.operationLog.support.debugger.DebugProcessor;
import cn.zhuguoqing.operationLog.service.ILogInsertAndImportService;
import cn.zhuguoqing.operationLog.service.IExcludeColumnGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author guoqing.zhu
 *     <p>description:关于日志的基础方法类;
 */
@Component
public class OperationLogsService {

  @Autowired private IOperationLogQueryService iOperationLogService;

  @Autowired protected ILogInsertAndImportService baseLogInfoService;

  @Autowired private IExcludeColumnGetService excludeColumnGetService;

  @Autowired private DebugProcessor debugProcessor;

  @Autowired private ModifyColCommentValueFactory modifyColCommentValueFactory;

  /**
   * 根据表名获取表字段及其注释
   *
   * @param logTable 表名称,表名称需要设置为:schemaA.tableB否则会抛出异常
   * @return 返回字段及其字段注释;
   */
  public Map<String, Object> getColumnCommentMap(String logTable) {
    Map<String, Object> columnCommentMap = new HashMap<>();
    if (!logTable.contains(".")) {
      throw new IllegalArgumentException("schemaTableName的格式需要设置为:schemaA.tableB");
    }
    String[] split = logTable.split("\\.");
    SchemaTableNameDTO schemaTableNameDTO = new SchemaTableNameDTO();
    schemaTableNameDTO.setSchemaName(split[0]);
    schemaTableNameDTO.setTableName(split[1]);
    List<ColumnCommentDTO> columnCommentDTOList =
        iOperationLogService.selectColumnCommentByTable(schemaTableNameDTO);
    for (ColumnCommentDTO cc : columnCommentDTOList) {
      columnCommentMap.put(cc.getColumn(), cc.getComment());
    }
    return columnCommentMap;
  }

  /**
   * 获取组装的SQL
   *
   * @param logTable 表名
   * @param idName id的名称;
   * @param idRef id的值;
   * @param cloum 字段数组
   * @return 组装好的SQL
   */
  public String getAssembleSQL(String logTable, String idName, String idRef, String[] cloum) {

    List<String> columnList = new ArrayList<>(cloum.length);
    for (String s : cloum) {
      columnList.add(s);
    }
    columnList.removeAll(excludeColumnGetService.getExcludeColumn());
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT ");
    for (int i = 0; i < columnList.size(); i++) {
      if (i == 0) {
        sql.append("`" + columnList.get(i) + "` ");
      } else {
        sql.append(",`" + columnList.get(i) + "` ");
      }
    }
    sql.append(" FROM " + logTable + " WHERE " + idName + " = '" + idRef + "'");
    return sql.toString();
  }


  /**
   * 插入日志及其日志详情,用于复杂的更新操作(需要记录旧值和新值的操作)
   *
   * @param schemaTable 表名
   * @param oldMap 旧值
   * @param newMap 新值
   * @param operation OperationLogDTO实体,里面封装了注解解析后的值
   * @param cloum 需要记录的字段
   * @param columnCommentMap 字段及其注释
   * @param expressionValues expression值Map
   * @param action 切点执行成功或者失败的执行过程解析完成后的值
   * @param operator 需要记录的用户信息
   * @param success 切点执行是否成功或者失败
   */
  public void insertLogAndLogDetail(
      String schemaTable,
      Map<String, Object> oldMap,
      Map<String, Object> newMap,
      OperationLogDTO operation,
      String[] cloum,
      Map<String, Object> columnCommentMap,
      Map<String, String> expressionValues,
      String action,
      Operator operator,
      boolean success) {
    // 插入日志主表
    if (oldMap != null && newMap != null) {
      insertLog(operation, expressionValues, action, operator, null, success);
      // 插入日志详细表
      if (success) {
        insertLogDetail(schemaTable, cloum, oldMap, newMap, columnCommentMap);
      }
    }
  }

  /**
   * 插入日志主表
   *
   * @param operation OperationLogDTO实体,里面封装了注解解析后的值
   * @param expressionValues  expression值Map
   * @param action 切点执行成功或者失败的执行过程解析完成后的值
   * @param operator 需要记录的用户
   * @param fileName 文件名称
   * @param success 切点是否成功
   */
  public void insertLog(
      OperationLogDTO operation,
      Map<String, String> expressionValues,
      String action,
      Operator operator,
      String fileName,
      boolean success) {
    OperationLogDomain op = new OperationLogDomain();
    op.setId(LogRecordContext.getVariable("logId").toString());
    op.setName(operation.getName());
    op.setTableName(operation.getTable());
    op.setTableId(expressionValues.get(operation.getIdRef()));
    op.setType(operation.getType().getType());
    op.setAction(expressionValues.get(action));
    op.setDetail(expressionValues.get(operation.getDetail()));
    op.setOperatorId(operator.getOperatorId());
    op.setOperatorName(operator.getOperatorName());
    op.setOperatorIp(operator.getOperatorIp());
    op.setOperationTime(new Timestamp(System.currentTimeMillis()));
    op.setImportFilename(fileName);
    op.setIfSuccess(success ? 1 : 0);
    baseLogInfoService.insertOperationLog(op);
  }

  /**
   * 插入日志详情表
   * @param schemaTable 表名
   * @param cloum 需要记录的字段
   * @param oldMap 旧值
   * @param newMap 新值
   * @param columnCommentMap 字段及其注释
   */
  public void insertLogDetail(
      String schemaTable,
      String[] cloum,
      Map<String, Object> oldMap,
      Map<String, Object> newMap,
      Map<String, Object> columnCommentMap) {
    List<OperationLogDetailDomain> opds = new ArrayList<>();
    for (String clm : cloum) {
      Object oldclm = oldMap.get(clm);
      Object newclm = newMap.get(clm);

      if (oldclm != null
          && newclm != null
          && Objects.equals(oldclm.toString(), newclm.toString())) {
        continue;
      }
      if (oldclm == null && newclm == null) {
        continue;
      }
      OperationLogDetailDomain opd = new OperationLogDetailDomain();

      // 获取自定义的函数
      String col = schemaTable + "." + clm;
      IModifyColCommentValueService keyService =
          modifyColCommentValueFactory.getService(CustomFunctionType.KEY, col);
      IModifyColCommentValueService valueService =
          modifyColCommentValueFactory.getService(CustomFunctionType.VALUE, col);
      opd.setClmName(clm);
      setOldColValue(opd, oldclm, valueService);
      setNewColValue(opd, newclm, valueService);
      setColComment(opd, clm, columnCommentMap, keyService);
      opd.setOperationLogId(LogRecordContext.getVariable("logId").toString());
      opds.add(opd);
    }
    if (!opds.isEmpty()) {
      baseLogInfoService.insertOperationLogDetail(opds);
    }
  }

  /**
   * 设置旧值
   * @param opd  OperationLogDetailDomain 封装了字段的名字,值和注释
   * @param clm  旧值
   * @param service 全局修改值的接口
   */
  private void setOldColValue(
      OperationLogDetailDomain opd, Object clm, IModifyColCommentValueService service) {
    if (clm != null) {
      String s = clm.toString();
      if (service != null) {
        s = service.modify(s);
      }
      opd.setOldString(s);
    } else {
      opd.setOldString("");
    }
  }

  /**
   * 设置新值
   * @param opd OperationLogDetailDomain 封装了字段的名字,值和注释
   * @param clm 新值
   * @param service 全局修改值的接口
   */
  private void setNewColValue(
      OperationLogDetailDomain opd, Object clm, IModifyColCommentValueService service) {
    if (clm != null) {
      String s = clm.toString();
      if (service != null) {
        s = service.modify(s);
      }
      opd.setNewString(s);
    } else {
      opd.setNewString("");
    }
  }

  /**
   * 设置字段注释
   * @param opd OperationLogDetailDomain 封装了字段的名字,值和注释
   * @param clm 注释
   * @param columnCommentMap  字段及其注释
   * @param service 全局修改注释的接口
   */
  private void setColComment(
      OperationLogDetailDomain opd,
      Object clm,
      Map<String, Object> columnCommentMap,
      IModifyColCommentValueService service) {
    Object o = columnCommentMap.get(clm.toString());
    if (o != null) {
      String s = o.toString();
      if (service != null) {
        s = service.modify(s);
      }
      opd.setClmComment(s);
    } else {
      String s = "";
      if (service != null) {
        s = service.modify(null);
      }
      opd.setClmComment(s);
    }
  }

  /**
   * 插入日志详情(用于外部自定义函数接口)
   *
   * @param oldList 旧值
   * @param newList 新值
   * @param clmName 需要被记录的字段名称
   */
  public void insertLogDetail(List<String> oldList, List<String> newList, String clmName) {
    try {
      OperationLogDetailDomain opd = new OperationLogDetailDomain();
      if (!CollectionUtils.isEmpty(oldList)) {
        StringBuilder old = new StringBuilder();
        for (int i = 0; i < oldList.size(); i++) {
          if (i == oldList.size() - 1) {
            old.append(oldList.get(i));
          } else {
            old.append(oldList.get(i));
            old.append(",");
          }
        }
        opd.setOldString(old.toString());
      }
      if (!CollectionUtils.isEmpty(newList)) {
        StringBuilder newBuilder = new StringBuilder();
        for (int i = 0; i < newList.size(); i++) {
          if (i == newList.size() - 1) {
            newBuilder.append(newList.get(i));
          } else {
            newBuilder.append(newList.get(i));
            newBuilder.append(",");
          }
        }
        opd.setNewString(newBuilder.toString());
      }
      if (opd.getOldString() == null && opd.getNewString() == null) {
        return;
      }
      if (opd.getOldString() != null
          && opd.getNewString() != null
          && Objects.equals(opd.getOldString(), opd.getNewString())) {
        return;
      }
      opd.setClmComment(clmName);
      opd.setOperationLogId(LogRecordContext.getVariable("logId").toString());
      List<OperationLogDetailDomain> opds = new ArrayList<>();
      opds.add(opd);
      baseLogInfoService.insertOperationLogDetail(opds);
    } catch (Exception e) {
      debugProcessor.error("insertLogDetail error", e);
    }
  }

  /**
   * 插入文件
   * @param recordId 文件id
   * @param originalFilename 文件名称
   * @param bytes 字节数组
   */
  public void insert(Long recordId, String originalFilename, byte[] bytes) {
    ImportFileDTO importFileDTO = new ImportFileDTO();
    importFileDTO.setFileId(recordId);
    importFileDTO.setFileName(originalFilename);
    importFileDTO.setBytes(bytes);
    baseLogInfoService.importFile(importFileDTO);
  }
}
