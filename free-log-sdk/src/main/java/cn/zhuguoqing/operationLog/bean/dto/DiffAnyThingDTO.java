package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/2 23:10
 * @Desription: TODO
 * @Version: 1.0
 */
@Data
@ToString
public class DiffAnyThingDTO {

    private Map<String, String> columnCommentMap;

    private List<Map<String, Object>> oldValueMap;

    private List<Map<String, Object>> newValueMap;

    private List<String> columns;

    private DiffDTO diffDTO;

    private String sql;

}
