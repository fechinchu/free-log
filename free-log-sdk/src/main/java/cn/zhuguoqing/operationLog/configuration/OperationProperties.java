package cn.zhuguoqing.operationLog.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author guoqing.zhu
 *     <p>description:日志的属性类,用于属性自定义
 */
@Component
@ConfigurationProperties("operation.log")
@Data
public class OperationProperties {

  private String importUrl;
}
