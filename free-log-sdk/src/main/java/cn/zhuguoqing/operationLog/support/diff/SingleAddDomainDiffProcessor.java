package cn.zhuguoqing.operationLog.support.diff;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.dto.DiffDTO;
import cn.zhuguoqing.operationLog.bean.enums.DiffType;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import com.google.common.annotations.Beta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author guoqing.zhu
 *     <p>description:数据增加后执行的策略
 */
@Component
@Slf4j
@Beta
public class SingleAddDomainDiffProcessor extends AbstractDiffProcessorTemplate {

  /** 插入后执行的策略 */
  @Override
  public void afterInsert(DiffDTO dto) {
    // 1.获取注释
    try {
      Map<String, String> columnCommentMap = getColumnComment(dto.getSchema(), dto.getTable());
      // 2.获取到表的字段
      List<String> colums = getColums(columnCommentMap, dto);
      // 3.组装SQL
      String sqlStr = appendSQL(colums, dto, null);
      // 4.获取查询结果
      List<Map<String, Object>> newMaps = getQueryResult(sqlStr);

      if (!CollectionUtils.isEmpty(newMaps)) {
        List<OperationLogDetailDomain> opds = new ArrayList<>();
        for (Map<String, Object> newMap : newMaps) {
          if (!CollectionUtils.isEmpty(newMap)) {
            Set<String> keySet = newMap.keySet();
            for (String s : keySet) {
              OperationLogDetailDomain opd = new OperationLogDetailDomain();
              setClmValue(opd, s, null, newMap.get(s) == null ? "" : newMap.get(s).toString(), dto);
              setClmComment(opd, s, columnCommentMap.get(s), dto);
              opd.setOperationLogId(LogRecordContext.getVariable("logId").toString());
              opds.add(opd);
            }
          }
        }
        if (!opds.isEmpty()) {
          baseLogInfoService.insertOperationLogDetail(opds);
        }
      }
    } catch (Exception e) {
      debugProcessor.error("SingleAddDomainDiffProcessor afterInsert error", e);
    }
  }

  @Override
  public DiffType getDiffType() {
    return DiffType.SINGLE_ADD;
  }
}
