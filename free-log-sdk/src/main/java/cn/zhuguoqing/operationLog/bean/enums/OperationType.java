package cn.zhuguoqing.operationLog.bean.enums;

/**
 * @Author:guoqing.zhu
 * @Date：2021/11/30
 * @Desription: TODO
 * @Version: 1.0
 */
public enum OperationType {

	ADD,
	UPDATE,
	COMPLEX_UPDATE,
	DELETE,
	IMPORT;


	public String getType() {
		if (this.equals(ADD)) {
			return "ADD";
		}
		if (this.equals(UPDATE)) {
			return "UPDATE";
		}
		if (this.equals(DELETE)) {
			return "DELETE";
		}
		if(this.equals(IMPORT)){
			return "IMPORT";
		}
		if(this.equals(COMPLEX_UPDATE)){
			return "COMPLEX_UPDATE";
		}
		return null;
	};
}
