package cn.zhuguoqing.operationLog.support.parser;

import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import cn.zhuguoqing.operationLog.bean.dto.OperationLogDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * @Author:guoqing.zhu
 * @Date：2021/11/26 15:40
 * @Desription: TODO
 * @Version: 1.0
 */
@Component
public class LogRecordOperationSourceParser {

    public OperationLogDTO parseLogRecordAnnotation(OperationLog recordAnnotation) {
        OperationLogDTO recordOps = OperationLogDTO.builder()
                .name(recordAnnotation.name())
                .success(recordAnnotation.success())
                .fail(recordAnnotation.fail())
                .table(recordAnnotation.table())
                .idName(recordAnnotation.idName())
                .idRef(recordAnnotation.idRef())
                .column(Arrays.asList(recordAnnotation.includeColumn()))
                .type(recordAnnotation.type())
//                .relativeTable(Arrays.asList(recordAnnotation.relativeTable()))
                .detail(recordAnnotation.detail())
                .condition(recordAnnotation.condition())
                .customMethod(recordAnnotation.customMethod())
                .build();
        return recordOps;
    }

}
