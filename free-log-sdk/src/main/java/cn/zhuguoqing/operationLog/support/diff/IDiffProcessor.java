package cn.zhuguoqing.operationLog.support.diff;


import cn.zhuguoqing.operationLog.bean.dto.DiffAnyThingDTO;
import cn.zhuguoqing.operationLog.bean.dto.DiffDTO;
import cn.zhuguoqing.operationLog.bean.enums.DiffType;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/9 16:04
 * @Desription: TODO
 * @Version: 1.0
 */
public interface IDiffProcessor {

    /**
     * SQL操作之前执行,单数据比较
     * @return
     */
    DiffAnyThingDTO beforeUpdate(DiffDTO dto);

    /**
     * SQL操作之后执行
     */
    void afterUpdate(DiffAnyThingDTO dto,String...newkeyValue);

    /**
     * SQL操作之后执行
     * @param dto
     */
    void afterInsert(DiffDTO dto);

    /**
     * 获取DiffType用于工厂方法
     * @return
     */
    DiffType getDiffType();


}
