package cn.zhuguoqing.operationLog.support.service.impl;

import cn.zhuguoqing.operationLog.bean.dto.ColumnCommentDTO;
import cn.zhuguoqing.operationLog.bean.dto.SchemaTableNameDTO;
import cn.zhuguoqing.operationLog.support.service.IOperationLogQueryService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/7 17:08
 * @Desription: TODO
 * @Version: 1.0
 */
@Slf4j
public class DefaultOperationLogQueryServiceImpl implements IOperationLogQueryService {

    @Override
    public Map<String, Object> selectAnyTable(String sql) {
        log.info("selectAnyTable:{}",sql);
        return null;
    }

    @Override
    public List<Map<String, Object>> selectAnyTableList(String sql) {
        log.info("selectAnyTableList:{}",sql);
        return null;
    }

    @Override
    public List<ColumnCommentDTO> selectColumnCommentByTable(SchemaTableNameDTO dto) {
        log.info("selectColumnCommentByTable:{}",dto);
        return null;
    }
}
