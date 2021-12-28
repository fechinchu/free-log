package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author guoqing.zhu
 *     <p>description:封装Diff的所有信息,用于Diff比较
 * @see cn.zhuguoqing.operationLog.support.diff.AbstractDiffProcessorTemplate
 * @see cn.zhuguoqing.operationLog.support.diff.IDiffProcessor
 */
@Data
@ToString
public class DiffAnyThingDTO {

  /** 字段及其注释 */
  private Map<String, String> columnCommentMap;

  /** 旧值 */
  private List<Map<String, Object>> oldValueMap;

  /** 新值 */
  private List<Map<String, Object>> newValueMap;

  /** 需要记录的字段 */
  private List<String> columns;

  /** Diff的封装 */
  private DiffDTO diffDTO;

  /** 组装好的SQL语句 */
  private String sql;
}
