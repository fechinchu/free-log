package cn.zhuguoqing.operationLog.support.diff;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.dto.ColumnCommentDTO;
import cn.zhuguoqing.operationLog.bean.dto.DiffAnyThingDTO;
import cn.zhuguoqing.operationLog.bean.dto.DiffDTO;
import cn.zhuguoqing.operationLog.bean.dto.SchemaTableNameDTO;
import cn.zhuguoqing.operationLog.bean.enums.CustomFunctionType;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import cn.zhuguoqing.operationLog.support.debugger.DebugProcessor;
import cn.zhuguoqing.operationLog.support.service.IBaseLogInfoService;
import cn.zhuguoqing.operationLog.support.service.IExcludeColumnGetService;
import cn.zhuguoqing.operationLog.support.service.IModifyColCommentValueService;
import cn.zhuguoqing.operationLog.support.service.IOperationLogQueryService;
import cn.zhuguoqing.operationLog.support.service.impl.ModifyColCommentValueFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/3 10:44
 * @Desription: TODO
 * @Version: 1.0
 */

@Slf4j
public abstract class AbstractDiffProcessorTemplate implements IDiffProcessor {

    @Autowired
    protected DebugProcessor debugProcessor;

    @Autowired
    protected IOperationLogQueryService iOperationLogService;

    @Autowired
    protected IExcludeColumnGetService excludeColumnGetService;

    @Autowired
    protected IBaseLogInfoService baseLogInfoService;

    @Autowired
    protected ModifyColCommentValueFactory modifyColCommentValueFactory;

    @Override
    public DiffAnyThingDTO beforeUpdate(DiffDTO dto) {
        try {
            //1.获取注释
            Map<String, String> columnCommentMap = getColumnComment(dto.getSchema(), dto.getTable());
            //2.获取到表的字段
            List<String> colums = getColums(columnCommentMap, dto);
            //3.组装SQL
            String sqlStr = appendSQL(colums, dto, null);
            //4.获取查询结果
            List<Map<String, Object>> queryResult = getQueryResult(sqlStr);
            //5.构建DiffAnyThingDTO
            return buildDiffAnyThingDTO(colums, queryResult, null, columnCommentMap, sqlStr, dto);
        } catch (Exception e) {
            debugProcessor.error("ListAddAndDeleteDiffProcessor beforeListUpdate error", e);
            return null;
        }
    }

    @Override
    public void afterUpdate(DiffAnyThingDTO diffAnyThingDTO, String... newKeyValue) {
        if (Objects.isNull(diffAnyThingDTO)) {
            return;
        }
        try {
            String sql = null;
            if (newKeyValue == null || newKeyValue.length == 0) {
                sql = diffAnyThingDTO.getSql() + " for update ";
            } else {
                sql = appendSQL(diffAnyThingDTO.getColumns(), diffAnyThingDTO.getDiffDTO(), newKeyValue) + " for update ";
            }
            List<Map<String, Object>> newMaps = iOperationLogService.selectAnyTableList(sql);
            diffAnyThingDTO.setNewValueMap(newMaps);
            diffAndRecord(diffAnyThingDTO);
        } catch (Exception e) {
            debugProcessor.error("afterUpdateBuild", e);
        }
    }

    /**
     * 模板方法
     *
     * @param diffAnyThingDTO
     */
    public void diffAndRecord(DiffAnyThingDTO diffAnyThingDTO) {
        debugProcessor.error("该类不支持该diffAndRecord的方法", null);
    }

    @Override
    public void afterInsert(DiffDTO dto) {
        debugProcessor.error("该类不支持该diffAndRecord的方法", null);
    }

    protected List<String> getColums(Map<String, String> columnCommentMap, DiffDTO dto) {
        //2.获取到表的字段
        List<String> cloums = null;
        if (CollectionUtils.isEmpty(dto.getIncludeRecordClms())) {
            Set<String> keySet = columnCommentMap.keySet();
            cloums = new ArrayList<>(keySet);
            cloums.removeAll(excludeColumnGetService.getExcludeColumn());
            if (!CollectionUtils.isEmpty(dto.getExcludeRecordClms())) {
                cloums.removeAll(dto.getExcludeRecordClms());
            }
        } else {
            cloums = dto.getIncludeRecordClms();
        }
        return cloums;
    }

    protected String appendSQL(List<String> colums, DiffDTO dto, String[] newKeyValue) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        for (int i = 0; i < colums.size(); i++) {
            if (i == 0) {
                sql.append("`" + colums.get(i) + "` ");
            } else {
                sql.append(",`" + colums.get(i) + "` ");
            }
        }

        sql.append(" FROM " + dto.getSchemaTableName() + " WHERE " + dto.getKeyName() + " in ");
        sql.append(" ( ");
        List<String> keyValue = new ArrayList<>();
        if (newKeyValue == null || newKeyValue.length == 0) {
            keyValue = dto.getKeyValue();
        }else{
            keyValue = Lists.newArrayList(newKeyValue);
        }
        for (int i = 0; i < keyValue.size(); i++) {
            if (i != keyValue.size() - 1) {
                sql.append("'");
                sql.append(keyValue.get(i));
                sql.append("'");
                sql.append(",");
            } else {
                sql.append("'");
                sql.append(keyValue.get(i));
                sql.append("'");
            }
        }
        sql.append(" ) ");
        sql.append(dto.getAppendSQLAfterWhere());
        log.info("CustomDiffSqlResultBuildProcessor:SQL:{}", sql.toString());
        return sql.toString();
    }

    protected Map<String, String> getColumnComment(String schemaName, String tableName) {
        Map<String, String> columnCommentMap = new HashMap<>();
        SchemaTableNameDTO schemaTableNameDTO = new SchemaTableNameDTO();
        schemaTableNameDTO.setTableSchema(schemaName);
        schemaTableNameDTO.setTableName(tableName);
        List<ColumnCommentDTO> columnCommentDTOS = iOperationLogService.selectColumnCommentByTable(schemaTableNameDTO);
        for (ColumnCommentDTO cc : columnCommentDTOS) {
            columnCommentMap.put(cc.getColumn(), cc.getComment());
        }
        return columnCommentMap;
    }

    protected List<Map<String, Object>> getQueryResult(String sql) {
        return iOperationLogService.selectAnyTableList(sql);
    }

    protected DiffAnyThingDTO buildDiffAnyThingDTO(List<String> colums, List<Map<String, Object>> oldMap, List<Map<String, Object>> newMap, Map<String, String> columnCommentMap, String sqlStr, DiffDTO dto) {
        DiffAnyThingDTO diffAnyThingDTO = new DiffAnyThingDTO();
        diffAnyThingDTO.setColumns(colums);
        diffAnyThingDTO.setOldValueMap(oldMap);
        diffAnyThingDTO.setNewValueMap(newMap);
        diffAnyThingDTO.setColumnCommentMap(columnCommentMap);
        diffAnyThingDTO.setSql(sqlStr);
        diffAnyThingDTO.setDiffDTO(dto);
        return diffAnyThingDTO;
    }

    protected List<OperationLogDetailDomain> getSingleDiff(DiffAnyThingDTO diffAnyThingDTO) {
        List<Map<String, Object>> oldMap = diffAnyThingDTO.getOldValueMap();
        List<Map<String, Object>> newMap = diffAnyThingDTO.getNewValueMap();
        Map<String, String> columnCommentMap = diffAnyThingDTO.getColumnCommentMap();
        List<String> columns = diffAnyThingDTO.getColumns();

        List<OperationLogDetailDomain> opds = new ArrayList<>();
        //2.遍历要比较的字段
        for (String column : columns) {

            String comment = columnCommentMap.get(column);
            String oldClm = null;
            String newClm = null;
            //取旧值
            if (oldMap != null) {
                oldClm = getSimgeClmValueBySingleParam(oldMap, column);
            }
            //取新值
            if (newMap != null) {
                newClm = getSimgeClmValueBySingleParam(newMap, column);
            }

            if (StringUtils.isEmpty(oldClm) && StringUtils.isEmpty(newClm)) {
                continue;
            }
            if (!StringUtils.isEmpty(oldClm) && !StringUtils.isEmpty(newClm) && oldClm.equals(newClm)) {
                continue;
            }
            OperationLogDetailDomain opd = new OperationLogDetailDomain();
            setClmValue(opd, column, oldClm, newClm, diffAnyThingDTO.getDiffDTO());
            setClmComment(opd, column, comment, diffAnyThingDTO.getDiffDTO());
            opd.setOperationLogId(LogRecordContext.getVariable("logId").toString());
            opds.add(opd);
        }
        return opds;
    }

    protected String getClmComment(String comment, String key, DiffDTO dto) {
        Map<String, Function<String, String>> customKeyFunctionMap = dto.getCustomCommentFunctionMap();
        //对于这种自定义方法也有优先级,首先在Diff所在的Session中定义的优先级最高,其次才是全局
        if (!CollectionUtils.isEmpty(customKeyFunctionMap) && customKeyFunctionMap.containsKey(key)) {
            Function<String, String> function = customKeyFunctionMap.get(key);
            return comment = function.apply(comment);
        } else {
            IModifyColCommentValueService service = modifyColCommentValueFactory.getService(CustomFunctionType.KEY, key);
            if (service != null) {
                return service.modify(comment);
            }
        }
        return comment;
    }

    protected String getClmValue(String value, String key, DiffDTO dto) {
        Map<String, Function<String, String>> customValueFunctionMap = dto.getCustomValueFunctionMap();
        if (!CollectionUtils.isEmpty(customValueFunctionMap) && customValueFunctionMap.containsKey(key)) {
            Function<String, String> function = customValueFunctionMap.get(key);
            return value = function.apply(value);
        } else {
            String schemaTableName = dto.getSchemaTableName();
            IModifyColCommentValueService service = modifyColCommentValueFactory.getService(CustomFunctionType.VALUE, schemaTableName + "." + key);
            if (service != null) {
                return service.modify(value);
            }
        }
        return value;
    }

    protected void setClmComment(OperationLogDetailDomain opd, String key, String comment, DiffDTO dto) {
        Map<String, Function<String, String>> customKeyFunctionMap = dto.getCustomCommentFunctionMap();
        if (!CollectionUtils.isEmpty(customKeyFunctionMap) && customKeyFunctionMap.containsKey(key)) {
            Function<String, String> function = customKeyFunctionMap.get(key);
            comment = function.apply(comment);
        } else {
            String schemaTableName = dto.getSchemaTableName();
            IModifyColCommentValueService service = modifyColCommentValueFactory.getService(CustomFunctionType.KEY, schemaTableName + "." + key);
            if (service != null) {
                comment = service.modify(comment);
            }
        }
        opd.setClmComment(comment);
        if (CollectionUtils.isEmpty(customKeyFunctionMap) || !customKeyFunctionMap.containsKey(key)) {
            if (!StringUtils.isEmpty(dto.getInformationAboutWhat())) {
                comment = "关于:[" + dto.getInformationAboutWhat() + "]的" + comment;
                opd.setClmComment(comment);
            }
            if (!StringUtils.isEmpty(dto.getDiffName())) {
                opd.setClmComment(dto.getDiffName());
            }
        }
    }

    protected void setClmValue(OperationLogDetailDomain opd, String key, String oldValue, String newValue, DiffDTO dto) {
        Map<String, Function<String, String>> customValueFunctionMap = dto.getCustomValueFunctionMap();
        if (CollectionUtils.isEmpty(customValueFunctionMap) || !customValueFunctionMap.containsKey(key)) {
            String schemaTableName = dto.getSchemaTableName();
            IModifyColCommentValueService service = modifyColCommentValueFactory.getService(CustomFunctionType.VALUE, schemaTableName + "." + key);
            if (service != null) {

                if (!StringUtils.isEmpty(oldValue)) {
                    oldValue = service.modify(oldValue);
                }
                if (!StringUtils.isEmpty(newValue)) {
                    newValue = service.modify(newValue);
                }
            }
            opd.setOldString(Strings.nullToEmpty(oldValue));
            opd.setNewString(Strings.nullToEmpty(newValue));
            return;
        }
        Function<String, String> function = customValueFunctionMap.get(key);
        if (!StringUtils.isEmpty(oldValue)) {
            opd.setOldString(function.apply(oldValue));
        } else {
            opd.setOldString("");
        }
        if (!StringUtils.isEmpty(newValue)) {
            opd.setNewString(function.apply(newValue));
        } else {
            opd.setNewString("");
        }
    }

    protected String getSimgeClmValueBySingleParam(List<Map<String, Object>> paramMap, String column) {
        if (paramMap.size() >= 2) {
            log.warn("Single Domain is not only:oldMap:{}", paramMap);
        }
        Map<String, Object> map = paramMap.get(0);
        Object oldClmObj = map.get(column);
        if (oldClmObj != null) {
            return oldClmObj.toString();
        }
        return null;
    }


}
