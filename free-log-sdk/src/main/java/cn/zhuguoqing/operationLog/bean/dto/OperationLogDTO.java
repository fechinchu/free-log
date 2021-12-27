package cn.zhuguoqing.operationLog.bean.dto;

import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/1 16:37
 * @Desription: 用于OperationLog Build
 * @Version: 1.0
 */
@Data
@Builder
public class OperationLogDTO {

    /**
     * 业务名称
     */
    private String name;
    /**
     * 操作成功描述
     */
    private String success;
    /**
     * 操作失败描述
     */
    private String fail;
    /**
     * 详细信息
     */
    private String detail;
    /**
     * 表名
     */
    private String table;
    /**
     * id 在函数的字段的名
     */
    private String idName;
    /**
     * id 在函数的字段的值
     */
    private String idRef;
    /**
     * 需要记录的字段,如果不填写那么采用数据库的字段,
     */
    private List<String> column;
    /**
     * 操作类型
     */
    private OperationType type;
    /**
     * 日志记录的先决条件
     */
    private String condition;
    /**
     * 自定义函数操作
     */
    private String customMethod;
}
