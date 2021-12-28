package cn.zhuguoqing.operationLog.support.diff;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.dto.DiffAnyThingDTO;
import cn.zhuguoqing.operationLog.bean.dto.DiffDTO;
import cn.zhuguoqing.operationLog.bean.enums.DiffType;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import cn.zhuguoqing.operationLog.support.util.GetDiffUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author guoqing.zhu
 *     <p>description:列表增删改查执行策略
 */
@Component
@Slf4j
public class ListAddAndDeleteDiffProcessor extends AbstractDiffProcessorTemplate {

  /**
   * 执行Diff记录
   */
  @Override
  public void diffAndRecord(DiffAnyThingDTO diffAnyThingDTO) {
    try {
      log.info("List diffAndRecord start :quickSQLSaveDTO:{}", diffAnyThingDTO);
      List<Map<String, Object>> oldMap = diffAnyThingDTO.getOldValueMap();
      List<Map<String, Object>> newMap = diffAnyThingDTO.getNewValueMap();
      if (GetDiffUtil.checkIfDifferent(oldMap, newMap)) {
        return;
      }
      Map<String, String> columnCommentMap = diffAnyThingDTO.getColumnCommentMap();
      List<String> columns = diffAnyThingDTO.getColumns();
      String oldStr = null;
      String newStr = null;
      if (!CollectionUtils.isEmpty(oldMap)) {
        oldStr = buildStrFromMap(oldMap, columnCommentMap, columns, diffAnyThingDTO.getDiffDTO());
      }
      if (!CollectionUtils.isEmpty(newMap)) {
        newStr = buildStrFromMap(newMap, columnCommentMap, columns, diffAnyThingDTO.getDiffDTO());
      }
      List<OperationLogDetailDomain> opds = new ArrayList<>();
      OperationLogDetailDomain opd = new OperationLogDetailDomain();
      opd.setOldString(Strings.nullToEmpty(oldStr));
      opd.setNewString(Strings.nullToEmpty(newStr));
      opd.setClmComment(diffAnyThingDTO.getDiffDTO().getDiffName());
      opd.setOperationLogId(LogRecordContext.getVariable("logId").toString());
      opds.add(opd);
      log.info("+++++++++opds:{}", opds);
      baseLogInfoService.insertOperationLogDetail(opds);
    } catch (Exception e) {
      debugProcessor.error("ListAddAndDeleteDiffProcessor diffAndRecord error", e);
    }
  }

  /**
   * 构建好需要记录的格式
   */
  public String buildStrFromMap(
      List<Map<String, Object>> colMap,
      Map<String, String> columnCommentMap,
      List<String> columns,
      DiffDTO dto) {
    StringBuilder builder = new StringBuilder();
    int count = 1;
    for (Map<String, Object> map : colMap) {
      builder.append("-------" + count + "-------");
      builder.append("\n");
      for (String colum : columns) {
        String comment = getClmComment(columnCommentMap.get(colum), colum, dto);
        String value =
            getClmValue(map.get(colum) == null ? "" : map.get(colum).toString(), colum, dto);
        builder.append("[" + comment + " = " + value + "] ");
        builder.append("\n");
      }

      builder.append("\n");
      count++;
    }
    return builder.toString();
  }

  @Override
  public DiffType getDiffType() {
    return DiffType.LIST_ADD_DELETE;
  }
}
