package cn.zhuguoqing.operationLog.support.diff;


import cn.zhuguoqing.operationLog.bean.dto.DiffAnyThingDTO;
import cn.zhuguoqing.operationLog.bean.enums.DiffType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/21 10:09
 * @Desription: TODO
 * @Version: 1.0
 */
@Component
@Slf4j
public class ListUpdateDiffProcessor extends AbstractDiffProcessorTemplate{

    @Override
    public void diffAndRecord(DiffAnyThingDTO diffAnyThingDTO) {
        // TODO 暂时没有用到,后期用到可以补充
    }


    @Override
    public DiffType getDiffType() {
        return DiffType.LIST_UPDATE;
    }


}
