package cn.zhuguoqing.operationLog.bean.domain;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/2 10:11
 * @Desription: 用于DAO
 * @Version: 1.0
 */
@Data
@ToString
public class OperationLogDomain {

    /**
     * 主键id
     */
    private String id;
    /**
     * 操作业务名
     */
    private String name;
    /**
     * 操作表名
     */
    private String tableName;
    /**
     * 操作表id
     */
    private String tableId;
    /**
     * 操作类型,(添加ADD,删除DELETE,修改UPDATE,导入IMPORT)'
     */
    private String type;
    /**
     *是否成功 0:失败,1成功
     */
    private Integer ifSuccess;
    /**
     * 操作内容
     */
    private String action;
    /**
     * 操作详情
     */
    private String detail;
    /**
     * 操作人id
     */
    private String operatorId;
    /**
     * 操作人名
     */
    private String operatorName;

    /**
     * 操作人ip
     */
    private String operatorIp;
    /**
     * 操作时间
     */
    private Timestamp operationTime;
    /**
     * 导入的文件名称
     */
    private String importFilename;
}
