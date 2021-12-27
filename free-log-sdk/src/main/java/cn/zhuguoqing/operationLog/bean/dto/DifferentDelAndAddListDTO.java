package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/7 22:08
 * @Desription: TODO
 * @Version: 1.0
 */
@Data
public class DifferentDelAndAddListDTO<T> {

    private List<T> deletedList;

    private List<T> addList;
}
