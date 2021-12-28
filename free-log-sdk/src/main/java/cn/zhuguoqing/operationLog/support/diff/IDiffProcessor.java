package cn.zhuguoqing.operationLog.support.diff;

import cn.zhuguoqing.operationLog.bean.dto.DiffAnyThingDTO;
import cn.zhuguoqing.operationLog.bean.dto.DiffDTO;
import cn.zhuguoqing.operationLog.bean.enums.DiffType;

/**
 * @author guoqing.zhu
 *     <p>description:Diff接口
 */
public interface IDiffProcessor {

  /**
   * SQL操作之前执行,单数据比较,与下面的方法组合使用
   *
   * @param dto DiffDTO
   * @return DiffAnyThingDTO,用于下一个方法`afterUpdate()`的传参
   */
  DiffAnyThingDTO beforeUpdate(DiffDTO dto);

  /**
   * SQL操作之后执行,与上面的方法组合使用
   *
   * @param dto DiffDTO
   * @param newKeyValue 如果更新后的key值变了,就传递,否则不传,example:执行前:where id in (1,2),执行后:where id in
   *     (3,4),那么这里需要传递"3","4";
   */
  void afterUpdate(DiffAnyThingDTO dto, String... newKeyValue);

  /**
   * 插入操作之后执行,用于{@link SingleAddDomainDiffProcessor},一般单独使用
   *
   * @param dto DiffDTO
   */
  void afterInsert(DiffDTO dto);

  /**
   * 获取DiffType用于工厂方法
   *
   * @return 策略类型
   */
  DiffType getDiffType();
}
