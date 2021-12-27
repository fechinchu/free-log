package cn.zhuguoqing.operationLog.support.diff;

import cn.zhuguoqing.operationLog.bean.domain.OperationLogDetailDomain;
import cn.zhuguoqing.operationLog.bean.dto.DiffAnyThingDTO;
import cn.zhuguoqing.operationLog.bean.enums.DiffType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/9 13:39
 * @Desription: TODO
 * @Version: 1.0
 */
@Component
@Slf4j
public class SingleUpdateDomainDiffProcessor extends AbstractDiffProcessorTemplate {

    @Override
    public void diffAndRecord(DiffAnyThingDTO diffAnyThingDTO) {
        try {
            List<OperationLogDetailDomain> opds = getSingleDiff(diffAnyThingDTO);
            if (!opds.isEmpty()) {
                baseLogInfoService.insertOperationLogDetail(opds);
            }
        } catch (Exception e) {
            debugProcessor.error("DataFromScatteredTableSingleDomainDiffProcessor diffAndRecord error",e);
        }
    }

    @Override
    public DiffType getDiffType() {
        return DiffType.SINGLE_UPDATE;
    }
}
