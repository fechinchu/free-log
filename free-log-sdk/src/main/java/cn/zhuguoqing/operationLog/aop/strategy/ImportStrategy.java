package cn.zhuguoqing.operationLog.aop.strategy;


import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/6 09:11
 * @Desription: 导入的策略
 * @Version: 1.0
 */
@Component
@Slf4j
public class ImportStrategy extends AbstractBaseStrategyTemplate {

    @Override
    public OperationType getOperationLogType() {
        return OperationType.IMPORT;
    }

    @Override
    public String getFileName(Object[] args) {
        MultipartFile m = null;
        InputStream i = null;
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                m = (MultipartFile) arg;
                break;
            }
            if (arg instanceof InputStream) {
                i = (InputStream) arg;
                break;
            }
        }
        //如果m为空,
        if (Objects.isNull(m) && Objects.isNull(i)) {
            log.error("MultipartFile and InputStream is null");
            return null;
        }
        if (m != null) {
            return m.getOriginalFilename();
        } else {
            Object fileName = LogRecordContext.getVariable("fileName");
            if (fileName == null) {
                log.error("fileName is null");
                return null;
            }
            return fileName.toString();
        }

    }

    @SneakyThrows
    @Override
    public void afterRecordDoCustom(ProceedingJoinPoint p) {
        String logId = LogRecordContext.getVariable("logId").toString();
        //获取MultipartFile
        Object[] args = p.getArgs();
        MultipartFile m = null;
        InputStream i = null;
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                m = (MultipartFile) arg;
                break;
            }
            if (arg instanceof InputStream) {
                i = (InputStream) arg;
                break;
            }
        }
        //如果m为空,
        if (Objects.isNull(m) && Objects.isNull(i)) {
            log.error("MultipartFile and InputStream is null");
            return;
        }
        InputStream inputStream = i;
        if (m != null) {
            inputStream = m.getInputStream();
        }
        byte[] bytes = ByteStreams.toByteArray(inputStream);
        operationLogService.insert(Long.parseLong(logId), m.getOriginalFilename(), bytes);
    }


}
