package cn.zhuguoqing.demo.operationLog.modifyColCommentValue;

import cn.zhuguoqing.operationLog.bean.enums.CustomFunctionType;
import cn.zhuguoqing.operationLog.service.IModifyColCommentValueService;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 2021/12/29 16:36
 *
 * @author guoqing.zhu
 *     <p>description:
 */
@Component
public class MotifyColValue implements IModifyColCommentValueService {
  @Override
  public CustomFunctionType getType() {
    return CustomFunctionType.VALUE;
  }

  @Override
  public List<String> getName() {
    return Lists.newArrayList("mydb01.goods_spu.name");
  }

  @Override
  public String modify(String param) {
    //加入是数字字典的code,我们在这边调用数字字典的接口转化成数字字典的值;
    //这个demo做简单更改;
    return "商品名称:" + param + "!!!";
  }
}
