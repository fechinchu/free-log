package cn.zhuguoqing.demo.controller;

import cn.zhuguoqing.demo.common.model.BasicResult;
import cn.zhuguoqing.demo.pojo.GoodsCommonDomain;
import cn.zhuguoqing.demo.pojo.GoodsDomain;
import cn.zhuguoqing.demo.service.GoodsService;
import cn.zhuguoqing.operationLog.bean.annotation.OperationLog;
import cn.zhuguoqing.operationLog.bean.enums.OperationType;
import cn.zhuguoqing.operationLog.support.context.LogRecordContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** @Author:guoqing.zhu @Date：2021/12/28 14:39 @Desription: TODO @Version: 1.0 */
@RestController
public class GoodsController {

  @Autowired private GoodsService goodsService;

  /**
   * 添加商品
   *
   * @param goodsDomain
   * @return
   */
  @RequestMapping("addGoods")
  @OperationLog(
      name = "商品管理",
      success = "新增名称为:[{{#goodsDomain.name}}]的商品",
      fail = "新增名称为:[{{#goodsDomain.name}}]的商品失败,失败原因:{{#_errorMsg}}",
      type = OperationType.ADD)
  public BasicResult addGoods(@RequestBody @NotNull GoodsDomain goodsDomain) {
    goodsService.addGoods(goodsDomain);
    return BasicResult.success();
  }

  @RequestMapping("updateGoods")
  @OperationLog(
      type = OperationType.COMPLEX_UPDATE,
      name = "商品管理",
      success = "修改名称为{{#goodsDomain.name}}的商品",
      fail = "修改名称为:[{{#goodsDomain.name}}]的商品失败,失败原因:{{#_errorMsg}}",
      table = "mydb01.goods_spu",
      idRef = "{{#goodsDomain.id}}",
      includeColumn = {"name"})
  public BasicResult updateGoods(@RequestBody @NotNull GoodsDomain goodsDomain) {
    goodsService.updateGoods(goodsDomain);
    return BasicResult.success();
  }

  @RequestMapping("updateGood01")
  @OperationLog(
      type = OperationType.DELETE,
      name = "商品管理",
      success = "删除了名为{getGoodsNameById{#id}}",
      fail = "删除了名为{getGoodsNameById{#id}}失败,失败原因:{{#_errorMsg}}")
  public BasicResult deleteGoodsById(@RequestParam @NotNull String id) {
    goodsService.deleteGoodsById(id);
    return BasicResult.success();
  }

  @RequestMapping("commonOperation")
  @OperationLog(
      type = OperationType.UPDATE,
      name = "商品管理",
      success = "我是日志我最大",
      condition = "{{#id.equals('1')}}")
  public BasicResult commonOperation(@RequestParam @NotNull String id) {
    return BasicResult.success();
  }

  @RequestMapping("commonOperation02")
  @OperationLog(
      type = OperationType.UPDATE,
      name = "商品管理",
      success =
          "{{#goodsDomain.shelve==1?'上架':#goodsDomain.shelve == 2?'下架':'删除'}},{{#goodsDomain.name.equals('小家伙')}},"
              + "名称为:{{#goodsDomain.name}}的商品,上下架结果为:{{#result}},看看异步执行能不能获取到:{{#result01}}")
  public BasicResult commonOperation02(@RequestBody @NotNull GoodsCommonDomain goodsDomain) {
    String result = doSomething(goodsDomain);
    LogRecordContext.putVariable("result", result);
    new Thread(() -> LogRecordContext.putVariable("result01", "难受马飞")).run();
    return BasicResult.success();
  }

  private String doSomething(GoodsCommonDomain goodsDomain) {
    return "得不到的永远在躁动";
  }

  @RequestMapping("importSomething")
  @OperationLog(
      type = OperationType.IMPORT,
      name = "商品管理",
      success = "上传文件喽,文件名为:{{#importFile.getOriginalFilename()}}")
  public BasicResult importSomething(@NotNull(message = "请上传文件") MultipartFile importFile) {
    return BasicResult.success();
  }

  @RequestMapping("ifSuccess")
  @OperationLog(
      type = OperationType.UPDATE,
      name = "商品管理",
      success = "操作成功了呀!{{#_ret}}",
      fail = "操作失败了哟,{{#_errorMsg}}")
  public BasicResult<String> ifSuccess() {
     return BasicResult.success("冲啊!!!");
    //return BasicResult.fail("10087", "还是失败了");
  }
}
