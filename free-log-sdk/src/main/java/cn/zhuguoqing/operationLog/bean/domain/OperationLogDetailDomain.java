package cn.zhuguoqing.operationLog.bean.domain;

import lombok.Data;
import lombok.ToString;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/1
 * @Desription: 操作日志详情模型 用于DAO
 * @Version: 1.0
 */
@Data
@ToString
public class OperationLogDetailDomain {
	/** 主键id */
	private String id;
	/** 操作日志id */
	private String operationLogId;
	/** 字段名 */
	private String clmName;
	/** 字段描述 */
	private String clmComment;
	/** 旧值 */
	private String oldString;
	/** 新值 */
	private String newString;

}
