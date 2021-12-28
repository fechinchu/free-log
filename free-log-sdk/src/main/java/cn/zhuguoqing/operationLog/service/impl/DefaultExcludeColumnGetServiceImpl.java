package cn.zhuguoqing.operationLog.service.impl;

import cn.zhuguoqing.operationLog.service.IExcludeColumnGetService;
import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 * @author guoqing.zhu
 *     <p>description:默认实现
 */
public class DefaultExcludeColumnGetServiceImpl implements IExcludeColumnGetService {

  @Override
  public List<String> getExcludeColumn() {
    return Lists.newArrayList();
  }
}
