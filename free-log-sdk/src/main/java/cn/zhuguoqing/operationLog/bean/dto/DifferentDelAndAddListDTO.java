package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;

import java.util.List;

/**
 * @author guoqing.zhu
 *     <p>description:Diff后的已删除信息和添加的信息
 */
@Data
public class DifferentDelAndAddListDTO<T> {

  /** 已删除 */
  private List<T> deletedList;

  /** 新添加 */
  private List<T> addList;
}
