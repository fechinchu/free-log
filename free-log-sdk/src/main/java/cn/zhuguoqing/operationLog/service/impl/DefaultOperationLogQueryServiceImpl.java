package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.bean.dto.ColumnCommentDTO;
import cn.zhuguoqing.operationLog.bean.dto.SchemaTableNameDTO;
import cn.zhuguoqing.operationLog.mapper.OperationLogMapper;
import cn.zhuguoqing.operationLog.service.IOperationLogQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author guoqing.zhu
 *     <p>description:默认实现
 */
@Slf4j
public class DefaultOperationLogQueryServiceImpl implements IOperationLogQueryService {

  @Autowired private OperationLogMapper operationLogMapper;

  @Override
  public Map<String, Object> selectAnyTable(String sql) {
    return operationLogMapper.selectAnyTable(sql);
  }

  @Override
  public List<Map<String, Object>> selectAnyTableList(String sql) {
    return operationLogMapper.selectAnyTableList(sql);
  }

  @Override
  public List<ColumnCommentDTO> selectColumnCommentByTable(SchemaTableNameDTO dto) {
    return operationLogMapper.selectColumnCommentByTable(dto);
  }
}
