package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/14 16:53
 * @Desription: TODO
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
public class OperationDebugDTO {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 操作日志id
     */
    private Long operationLogId;

    /**
     * 错误信息定位Id,拿着这个Id去数据库找错误日志
     */
    private Long logPositioningId;

    /**
     * 错误信息
     */
    private String errorInfo;

    public OperationDebugDTO(Long operationLogId, Long logPositioningId, String errorInfo) {
        this.operationLogId = operationLogId;
        this.logPositioningId = logPositioningId;
        this.errorInfo = errorInfo;
    }
}
