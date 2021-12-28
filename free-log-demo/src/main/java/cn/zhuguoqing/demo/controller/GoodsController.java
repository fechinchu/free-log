package cn.zhuguoqing.demo.controller;

import cn.zhuguoqing.demo.common.model.BasicResult;
import cn.zhuguoqing.demo.pojo.GoodsDomain;
import cn.zhuguoqing.demo.service.GoodsService;
import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/28 14:39
 * @Desription: TODO
 * @Version: 1.0
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 添加商品
     *
     * @param goodsDomain
     * @return
     */
    @RequestMapping("addGoods")
    @OperationLog(name = "商品管理", success = "新增名称为:[{{#goodsDomain.name}}]的商品",
            fail = "新增名称为:[{{#goodsDomain.name}}]的商品失败,失败原因:{{#_errorMsg}}", type = OperationType.ADD)
    public BasicResult addGoods(@RequestBody @NotNull GoodsDomain goodsDomain) {
        goodsService.addGoods(goodsDomain);
        return BasicResult.success();
    }
}
