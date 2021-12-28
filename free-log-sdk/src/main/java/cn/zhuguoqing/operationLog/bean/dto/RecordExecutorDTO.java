package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author guoqing.zhu
 *     <p>description:复杂修改用的日志记录
 * @see cn.zhuguoqing.operationLog.aop.strategy.ComplexUpdateStrategy
 */
@Builder
@Getter
public class RecordExecutorDTO {

  private String schemaTable;

  /** 旧值 */
  private Map<String, Object> oldMap;

  /** 新值 */
  private Map<String, Object> newMap;

  /** 切点执行后的结果 */
  private Object ret;

  /** 目标方法 */
  private Method method;

  /** 参数 */
  private Object[] args;

  /** OperationLogDTO实体 */
  private OperationLogDTO operationLogDTO;

  /** 目标类 */
  private Class<?> targetClass;

  /** 是否成功 */
  private Boolean success;

  /** 错误信息 */
  private String errorMsg;

  /** 切点执行前的自定义方法Map */
  private Map<String, String> functionNameAndReturnMap;

  /** 字段及其注释的Map */
  private Map<String, Object> columnCommentMap;

  /** 字段数组 */
  private String[] cloum;
}
