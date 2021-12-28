package cn.zhuguoqing.operationLog.support.util;

import cn.zhuguoqing.operationLog.bean.dto.DifferentDelAndAddListDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guoqing.zhu
 *     <p>description:检查不同,获取不同的工具类
 */
public class GetDiffUtil {

  /**
   * 获取不同
   *
   * @param oldList 旧值集合
   * @param newList 新值集合
   * @return 新值了哪些值,删除了哪些值
   */
  public static <T> DifferentDelAndAddListDTO<T> getDifferentBetweenList(
      List<T> oldList, List<T> newList) {
    DifferentDelAndAddListDTO<T> dto = new DifferentDelAndAddListDTO<>();
    List<T> deletedList = new ArrayList<>();
    List<T> addList = new ArrayList<>();
    for (T t : oldList) {
      if (!newList.contains(t)) {
        deletedList.add(t);
      }
    }
    for (T t : newList) {
      if (!newList.contains(t)) {
        addList.add(t);
      }
    }
    dto.setDeletedList(deletedList);
    dto.setAddList(addList);
    return dto;
  }

  /**
   * 检查是否不同
   *
   * @param oldList 旧值集合
   * @param newList 新值集合
   * @return true为相同,false为不同
   */
  public static <T> Boolean checkIfDifferent(List<T> oldList, List<T> newList) {
    for (T t : oldList) {
      if (!newList.contains(t)) {
        return false;
      }
    }
    for (T t : newList) {
      if (!oldList.contains(t)) {
        return false;
      }
    }
    return true;
  }
}
