package cn.zhuguoqing.operationLog.bean.annotation;

import cn.zhuguoqing.operationLog.configuration.OperationAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author guoqing.zhu
 *     <p>description:开启日志功能注解,用在SpringBootStarter
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({OperationAutoConfiguration.class})
public @interface EnableOperationLog {

}
