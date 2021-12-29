package cn.zhuguoqing.demo.operationLog.parser;

import cn.zhuguoqing.demo.mapper.GoodsMapper;
import cn.zhuguoqing.demo.pojo.GoodsDomain;
import cn.zhuguoqing.operationLog.service.IParseFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 2021/12/29 17:05
 *
 * @author guoqing.zhu
 *     <p>description:
 */
@Component
public class GetGoodsNameByIdParser implements IParseFunction {

  @Autowired private GoodsMapper goodsMapper;


  @Override
  public boolean executeBefore() {
    return true;
  }

  @Override
  public String functionName() {
    return "getGoodsNameById";
  }

  @Override
  public String apply(String value) {
    GoodsDomain goodsDomain = goodsMapper.selectByPrimaryKey(value);
    if(goodsDomain!=null){
      return goodsDomain.getName();
    }
    return value;
  }
}
