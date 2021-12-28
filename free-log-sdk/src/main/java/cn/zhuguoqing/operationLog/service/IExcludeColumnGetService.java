package cn.zhuguoqing.operationLog.service;

import java.util.List;

/**
 * @author guoqing.zhu
 *     <p>description:设置全局的需要忽略的数据库字段,需要自己实现,如不实现,则不忽略
 * @see cn.zhuguoqing.operationLog.service.impl.DefaultExcludeColumnGetServiceImpl
 */
public interface IExcludeColumnGetService {

  /**
   * 获取排除的字段
   *
   * @return 返回需要排除的字段集合
   */
  List<String> getExcludeColumn();
}
