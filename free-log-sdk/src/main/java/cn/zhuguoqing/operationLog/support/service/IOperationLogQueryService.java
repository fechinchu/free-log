package cn.zhuguoqing.operationLog.support.service;


import cn.zhuguoqing.operationLog.bean.dto.ColumnCommentDTO;
import cn.zhuguoqing.operationLog.bean.dto.SchemaTableNameDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/7 17:08
 * @Desription: TODO
 * @Version: 1.0
 */
public interface IOperationLogQueryService {


    Map<String,Object> selectAnyTable(String sql);

    /**
     * 查询任意SQL获取List
     * @param sql
     * @return
     */
    List<Map<String,Object>> selectAnyTableList(String sql);

    /**
     * 查询任意表的字段与备注
     */
    List<ColumnCommentDTO> selectColumnCommentByTable(SchemaTableNameDTO dto);
}
