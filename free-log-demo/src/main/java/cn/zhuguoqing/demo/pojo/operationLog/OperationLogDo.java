package cn.zhuguoqing.demo.pojo.operationLog;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * <p>2021/12/28 15:50</p>
 *
 * @author guoqing.zhu
 * <P>description:</p>
 */
@Data
@Table(name="operation_log")
public class OperationLogDo {

    /**
     * 主键id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
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
