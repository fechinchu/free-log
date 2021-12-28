package cn.zhuguoqing.demo.pojo.operationLog;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "operation_log_detail")
public class OperationLogDetailDo {
    /**
     * 主键id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private String id;
    /**
     * 操作日志id
     */
    private String operationLogId;
    /**
     * 字段名
     */
    private String clmName;
    /**
     * 字段描述
     */
    private String clmComment;
    /**
     * 旧值
     */
    private String oldString;
    /**
     * 新值
     */
    private String newString;

}