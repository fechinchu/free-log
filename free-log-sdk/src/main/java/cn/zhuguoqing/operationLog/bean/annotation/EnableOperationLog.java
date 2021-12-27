package cn.zhuguoqing.operationLog.bean.annotation;

import cn.zhuguoqing.operationLog.support.configuration.OperationAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/27 14:57
 * @Desription: TODO
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({OperationAutoConfiguration.class})
public @interface EnableOperationLog {

}
