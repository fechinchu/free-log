package cn.zhuguoqing.operationLog.support.configuration;


import cn.zhuguoqing.operationLog.support.debugger.IDebugService;
import cn.zhuguoqing.operationLog.support.debugger.impl.DefaultDebugServiceImpl;
import cn.zhuguoqing.operationLog.support.service.*;
import cn.zhuguoqing.operationLog.support.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/3 13:59
 * @Desription: 参考SpringBoot的AutoConfiguration, 适用于自定义的IBaseLogInfoService和IOperatorGetService
 * 因为对于插入的接口放在common包中,不能依赖于别的微服务;
 * @Version: 1.0
 */
@Configuration
@Slf4j
@ComponentScan("cn.zhuguoqing.operationLog")
public class OperationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IBaseLogInfoService.class)
    public IBaseLogInfoService baseLogInfoService() {
        return new DefaultBaseLogInfoServiceImpl();
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
    @ConditionalOnMissingBean(ICheckResult.class)
    public ICheckResult checkResult(){
        return new DefaultCheckResultServiceImpl();
    }


}
