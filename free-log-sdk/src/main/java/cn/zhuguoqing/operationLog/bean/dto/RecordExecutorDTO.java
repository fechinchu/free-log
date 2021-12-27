package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/17 10:42
 * @Desription: TODO
 * @Version: 1.0
 */
@Builder
@Getter
public class RecordExecutorDTO {

    private String schemaTable;

    /**
     * 旧值
     */
    private Map<String,Object> oldMap;

    /**
     * 新值
     */
    private Map<String,Object> newMap;

    /**
     * proceed后的结果
     */
    private Object ret;

    /**
     * 目标方法
     */
    private Method method;

    /**
     * 参数
     */
    private Object[] args;

    /**
     * OperationLogDTO实体
     */
    private OperationLogDTO operationLogDTO;

    /**
     * 目标类
     */
    private Class<?> targetClass;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * proceed前的自定义方法Map
     */
    private Map<String, String> functionNameAndReturnMap;

    /**
     * 字段及其注释的Map
     */
    private Map<String, Object> columnCommentMap;

    /**
     * 字段数组
     */
    private String[] cloum;
}
