package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/3 17:21
 * @Desription: TODO
 * @Version: 1.0
 */
@Data
@ToString
public class SchemaTableNameDTO {

    private String tableSchema;

    private String tableName;
}
