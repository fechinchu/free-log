package cn.zhuguoqing.operationLog.aop.strategy;

import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import cn.zhuguoqing.operationLog.bean.dto.OperationLogDTO;
import cn.zhuguoqing.operationLog.bean.dto.Operator;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import cn.zhuguoqing.operationLog.support.util.SnowFlakeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author guoqing.zhu
 *     <p>description:日志记录策略的模板方法.对{@link AbstractOperationLogStrategy} 进一步具体化.后期一般简单的日志策略实现该接口即可
 */
@Slf4j
public abstract class AbstractBaseStrategyTemplate extends AbstractOperationLogStrategy {

  /**
   * @param p 切点
   * @param operationlog 自定义的注解
   * @return
   */
  @SneakyThrows
  @Override
  public Object operate(ProceedingJoinPoint p, OperationLog operationlog) {
    AtomicBoolean atomicBoolean = new AtomicBoolean(true);
    // 1.上下文对象放入空
    LogRecordContext.putEmptySpan();
    LogRecordContext.putVariable("logId", String.valueOf(SnowFlakeUtil.getId()));
    MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
    // 2.解析注解获取实体
    OperationLogDTO operationLogDTO = null;
    Map<String, String> functionNameAndReturnMap = null;
    try {
      operationLogDTO = logRecordOperationSourceParser.parseLogRecordAnnotation(operationlog);
      // 3.获取要解析的模板
      List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operationLogDTO);
      // 4.解析procceed之前的spEL模板,主要用于获取自定义方法
      functionNameAndReturnMap =
          processBeforeExecuteFunctionTemplate(
              spElTemplates, getTargetClass(p.getTarget()), getMethod(p), p.getArgs());
    } catch (Exception e) {
      atomicBoolean.compareAndSet(true, false);
      debugProcessor.error("log record parse before function exception", e);
    }

    // 5.执行切点
    Throwable throwable = null;
    Object proceed = null;
    try {
      proceed = p.proceed();
      // 拓展点,检测响应结果
      checkResult.checkResult(methodExecuteResult, proceed);
    } catch (Throwable e) {
      methodExecuteResult =
          new MethodExecuteResult(
              false, e, e.getMessage() == null ? "系统异常,请查看具体日志" : e.getMessage());
      debugProcessor.error("proceed exception", e);
      throwable = e;
    }
    // 6.执行记录
    try {
      if (atomicBoolean.get() && Objects.nonNull(operationLogDTO)) {
        recordExecute(
            proceed,
            getMethod(p),
            p.getArgs(),
            operationLogDTO,
            getTargetClass(p.getTarget()),
            methodExecuteResult.isSuccess(),
            methodExecuteResult.getErrorMsg(),
            functionNameAndReturnMap);
        // 执行完记录后执行自定义方法,可以用来扩展
        afterRecordDoCustom(p);
      }
    } catch (Exception t) {
      // 记录日志错误不要影响业务
      debugProcessor.error("log record parse exception", t);
    } finally {
      LogRecordContext.clear();
      atomicBoolean.set(true);
    }
    // 重新抛出异常
    if (throwable != null) {
      throw throwable;
    }
    return proceed;
  }

  /**
   * 记录日志
   *
   * @param ret 切点执行后的结果
   * @param method 方法
   * @param args 方法参数
   * @param operation {@link OperationLog}解析后得到的数据
   * @param targetClass 目标类
   * @param success 切点执行是否成功
   * @param errorMsg 如果失败四边的信息
   * @param functionNameAndReturnMap 自定义的方法及其结果
   */
  protected void recordExecute(
      Object ret,
      Method method,
      Object[] args,
      OperationLogDTO operation,
      Class<?> targetClass,
      boolean success,
      String errorMsg,
      Map<String, String> functionNameAndReturnMap) {

    String action = getActionContent(success, operation);
    // 获取需要解析的表达式
    List<String> spElTemplates = getSpElTemplates(operation, action);
    Map<String, String> expressionValues =
        processTemplate(
            spElTemplates, ret, targetClass, method, args, errorMsg, functionNameAndReturnMap);
    // condition is true
    if (logConditionPassed(operation.getCondition(), expressionValues)) {
      Operator operator = getOperator();
      String fileName = getFileName(args);
      operationLogService.insertLog(
          operation, expressionValues, action, operator, fileName, success);
    }
  }

  /**
   * 模板方法用于实现,从参数名获取文件名.一般只适用于文件导入的策略
   *
   * @param args 切点的参数
   * @return 文件名称
   */
  public String getFileName(Object[] args) {
    return null;
  }

  /**
   * 模板方法用于实现 执行完记录后执行自定义方法,可以用来扩展
   *
   * @param p 切点
   */
  public void afterRecordDoCustom(ProceedingJoinPoint p) {}
}
