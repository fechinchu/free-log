package cn.zhuguoqing.operationLog.support.diff;

import cn.zhuguoqing.operationLog.bean.enums.DiffType;
import cn.zhuguoqing.operationLog.support.debugger.DebugProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/9 16:22
 * @Desription: TODO
 * @Version: 1.0
 */
@Component
public class DiffProcessorFactory {

    @Autowired
    private DebugProcessor debugProcessor;

    @Autowired
    private List<IDiffProcessor> diffProcessors;

    public IDiffProcessor getDiffProcessor(DiffType type){
        if (type == null) {
            debugProcessor.error("DiffProcessorFactory type cannot be null",null);
            return null;
        }
        Optional<IDiffProcessor> processor = diffProcessors.stream().filter((s) -> type.equals(s.getDiffType())).findFirst();
        if (processor.isPresent()) {
            return processor.get();
        }
        debugProcessor.error("DiffProcessorFactory strategy cannot find",null);
        return null;
    }
}
