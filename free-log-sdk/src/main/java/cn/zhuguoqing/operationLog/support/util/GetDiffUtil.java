package cn.zhuguoqing.operationLog.support.util;

import cn.zhuguoqing.operationLog.bean.dto.DifferentDelAndAddListDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/7 21:58
 * @Desription: TODO
 * @Version: 1.0
 */
public class GetDiffUtil{

    public static<T> DifferentDelAndAddListDTO<T> getDifferentBetweenList(List<T> oldList, List<T> newList) {
        DifferentDelAndAddListDTO<T> dto = new DifferentDelAndAddListDTO<>();
        List<T> deletedList = new ArrayList<>();
        List<T> addList = new ArrayList<>();
        for (T t : oldList) {
            if(!newList.contains(t)){
                deletedList.add(t);
            }
        }
        for (T t : newList) {
            if(!newList.contains(t)){
                addList.add(t);
            }
        }
        dto.setDeletedList(deletedList);
        dto.setAddList(addList);
        return dto;
    }

    /**
     *
     * @param oldList
     * @param newList
     * @param <T>
     * @return true为相同,false为不同
     */
    public static<T> Boolean checkIfDifferent(List<T> oldList, List<T> newList){
        for (T t : oldList) {
            if(!newList.contains(t)){
                return false;
            }
        }
        for (T t : newList) {
            if(!oldList.contains(t)){
                return false;
            }
        }
        return true;
    }
}
