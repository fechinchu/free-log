package cn.zhuguoqing.operationLog.configuration;

import cn.zhuguoqing.operationLog.service.*;
import cn.zhuguoqing.operationLog.service.factory.ParseFunctionFactory;
import cn.zhuguoqing.operationLog.service.impl.*;
import cn.zhuguoqing.operationLog.support.debugger.IDebugService;
import cn.zhuguoqing.operationLog.support.debugger.impl.DefaultDebugServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author guoqing.zhu
 *     <p>description:自动配置类
 */
@Configuration
@Slf4j
@ComponentScan("cn.zhuguoqing.operationLog")
@MapperScan("cn.zhuguoqing.operationLog.mapper")
public class OperationAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(ILogInsertAndImportService.class)
  public ILogInsertAndImportService baseLogInfoService() {
    return new DefaultLogInsertAndImportServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean(IFunctionService.class)
  @Autowired
  public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
    return new DefaultFunctionServiceImpl(parseFunctionFactory);
  }

  @Bean
  @ConditionalOnMissingBean(IOperatorGetService.class)
  public IOperatorGetService operatorGetService() {
    return new DefaultOperatorGetServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean(IParseFunction.class)
  public IParseFunction parseFunction() {
    return new DefaultParseFunction();
  }

  @Bean
  @ConditionalOnMissingBean(IExcludeColumnGetService.class)
  public IExcludeColumnGetService excludeColumnGetService() {
    return new DefaultExcludeColumnGetServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean(IOperationLogQueryService.class)
  public IOperationLogQueryService operationLogService() {
    return new DefaultOperationLogQueryServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean(IDebugService.class)
  public IDebugService debugService() {
    return new DefaultDebugServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean(IModifyColCommentValueService.class)
  public IModifyColCommentValueService modifyColCommentValueService() {
    return new DefaultModifyColCommentValueServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean(ICheckResultService.class)
  public ICheckResultService checkResult() {
    return new DefaultCheckResultServiceImpl();
  }
}
