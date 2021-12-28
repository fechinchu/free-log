package cn.zhuguoqing.operationLog.support.parser;

import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import cn.zhuguoqing.operationLog.bean.dto.OperationLogDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author guoqing.zhu
 *     <p>description:注解构建{@link OperationLogDTO}
 */
@Component
public class LogRecordOperationSourceParser {

  public OperationLogDTO parseLogRecordAnnotation(OperationLog recordAnnotation) {
    OperationLogDTO recordOps =
        OperationLogDTO.builder()
            .name(recordAnnotation.name())
            .success(recordAnnotation.success())
            .fail(recordAnnotation.fail())
            .table(recordAnnotation.table())
            .idName(recordAnnotation.idName())
            .idRef(recordAnnotation.idRef())
            .column(Arrays.asList(recordAnnotation.includeColumn()))
            .type(recordAnnotation.type())
            .detail(recordAnnotation.detail())
            .condition(recordAnnotation.condition())
            .customMethod(recordAnnotation.customMethod())
            .build();
    return recordOps;
  }
}
