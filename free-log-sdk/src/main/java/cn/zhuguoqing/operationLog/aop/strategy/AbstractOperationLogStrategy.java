package cn.zhuguoqing.operationLog.aop.strategy;

import cn.zhuguoqing.operationLog.bean.dto.OperationLogDTO;
import cn.zhuguoqing.operationLog.bean.dto.Operator;
import cn.zhuguoqing.operationLog.support.debugger.DebugProcessor;
import cn.zhuguoqing.operationLog.support.debugger.IDebugService;
import cn.zhuguoqing.operationLog.support.parser.LogRecordOperationSourceParser;
import cn.zhuguoqing.operationLog.support.parser.LogRecordValueParser;
import cn.zhuguoqing.operationLog.service.ICheckResultService;
import cn.zhuguoqing.operationLog.service.IOperationLogQueryService;
import cn.zhuguoqing.operationLog.service.IOperatorGetService;
import cn.zhuguoqing.operationLog.service.impl.OperationLogsService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author guoqing.zhu
 *     <p>description:日志记录抽象方法,大部分日志的记录的公共方法都在这边
 */
@Slf4j
public abstract class AbstractOperationLogStrategy extends LogRecordValueParser
    implements OperationLogStrategy {

  @Autowired protected IDebugService iDebugService;

  @Autowired protected IOperationLogQueryService iOperationLogService;

  @Autowired protected OperationLogsService operationLogService;

  @Autowired protected LogRecordOperationSourceParser logRecordOperationSourceParser;

  @Autowired protected IOperatorGetService iOperatorGetService;

  @Autowired protected IDebugService debugService;

  @Autowired protected DebugProcessor debugProcessor;

  @Autowired protected ICheckResultService checkResult;

  /**
   * @param condition 先决条件
   * @param expressionValues spEL解析后得到的结果
   * @return 返回先决条件是否通过
   */
  protected boolean logConditionPassed(String condition, Map<String, String> expressionValues) {
    return StringUtils.isEmpty(condition)
        || StringUtils.endsWithIgnoreCase(expressionValues.get(condition), "true");
  }

  /**
   * @param success 切点是否执行成功
   * @param operation {@link cn.zhuguoqing.operationLog.bean.annotation.OperationLog}解析后的实体
   * @return 需要被记录的action值
   */
  protected String getActionContent(boolean success, OperationLogDTO operation) {
    String action = "";
    if (success) {
      action = operation.getSuccess();
    } else {
      action = operation.getFail();
    }
    return action;
  }

  /**
   * 获取要被记录的字段
   *
   * @param columnCommentMap 数据库中的原字段
   * @param cloum 需要被记录的字段
   * @param excludeColumn 需要被排除的字段
   * @return
   */
  protected String[] getColumnNeedRecord(
      Map<String, Object> columnCommentMap, String[] cloum, String[] excludeColumn) {
    if (cloum.length == 0) {
      Set<String> keySet = columnCommentMap.keySet();
      List<String> list = new ArrayList<>(keySet);
      if (excludeColumn.length != 0) {
        list.removeAll(new ArrayList<>(Arrays.asList(excludeColumn)));
      }
      cloum = list.toArray(new String[0]);
    }
    return cloum;
  }

  /**
   * 获取切点执行前需要解析的spEL模板,这里专门用于解析idRef,即字段值.
   *
   * @param operation OperationLogDTO
   * @return 模板集合
   */
  protected List<String> getProcessBeforeExecuteTemplate(OperationLogDTO operation) {
    List<String> spElTemplates = Lists.newArrayList();
    if (!StringUtils.isEmpty(operation.getIdRef())) {
      spElTemplates.add(operation.getIdRef());
    }
    return spElTemplates;
  }

  /**
   * 获取切点执行前需要解析的spEL模板
   *
   * @param operation OperationLogDTO
   * @return 模板集合
   */
  protected List<String> getBeforeExecuteFunctionTemplate(OperationLogDTO operation) {
    List<String> spElTemplates = new ArrayList<>();
    // 执行之前的函数，失败模版不解析
    List<String> templates = getSpElTemplates(operation, operation.getSuccess());
    if (!CollectionUtils.isEmpty(templates)) {
      spElTemplates.addAll(templates);
    }
    return spElTemplates;
  }

  /**
   * 获取 IdRef + detail + customMethod + condition 模板
   *
   * @param operation OperationLogDTO
   * @param action 成功或失败的记录
   * @return 模板
   */
  protected List<String> getSpElTemplates(OperationLogDTO operation, String action) {
    List<String> spElTemplates = Lists.newArrayList(action);
    if (!StringUtils.isEmpty(operation.getCondition())) {
      spElTemplates.add(operation.getCondition());
    }
    if (!StringUtils.isEmpty(operation.getIdRef())) {
      spElTemplates.add(operation.getIdRef());
    }
    if (!StringUtils.isEmpty(operation.getDetail())) {
      spElTemplates.add(operation.getDetail());
    }
    if (!StringUtils.isEmpty(operation.getCustomMethod())) {
      spElTemplates.add(operation.getCustomMethod());
    }

    return spElTemplates;
  }

  /** 获取目标类 */
  protected Class<?> getTargetClass(Object target) {
    return AopProxyUtils.ultimateTargetClass(target);
  }

  /** 根据切点获取方法 */
  @SneakyThrows
  protected Method getMethod(ProceedingJoinPoint p) {
    Signature sig = p.getSignature();
    MethodSignature msig = null;
    if (!(sig instanceof MethodSignature)) {
      throw new IllegalArgumentException("该注解只能用于方法");
    }
    msig = (MethodSignature) sig;
    Object target = p.getTarget();
    return target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
  }

  /** 获取用户 */
  protected Operator getOperator() {
    Operator operator = iOperatorGetService.getUser();
    if (Objects.isNull(operator)) {
      throw new IllegalArgumentException("[LogRecord] operator is null");
    }
    return operator;
  }

  /** 封装执行切点后的结果 */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MethodExecuteResult {
    private boolean success;
    private Throwable throwable;
    private String errorMsg;
  }
}
