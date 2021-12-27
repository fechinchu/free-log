package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;

/**
 * @Author:guoqing.zhu
 * @Date：2021/11/30
 * @Desription: TODO
 * @Version: 1.0
 */
@Data
public class ColumnCommentDTO {

    /**
     * 表字段
     */
    private String column;

    /**
     * 表字段注释
     */
    private String comment;

}
