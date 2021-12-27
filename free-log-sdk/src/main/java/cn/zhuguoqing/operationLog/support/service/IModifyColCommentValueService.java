package cn.zhuguoqing.operationLog.support.service;

import cn.zhuguoqing.operationLog.bean.enums.CustomFunctionType;

import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/16 18:06
 * @Desription: TODO
 * @Version: 1.0
 */
public interface IModifyColCommentValueService {

    /**
     * 获取类型
     * @return
     */
    CustomFunctionType getType();
    /**
     * 获取名称
     * @return
     */
    List<String> getName();

    /**
     * 转换
     * @param param
     * @return
     */
    String modify(String param);
}
