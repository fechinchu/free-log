package cn.zhuguoqing.operationLog.support.service;

import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/3 15:14
 * @Desription: TODO
 * @Version: 1.0
 */
public interface IExcludeColumnGetService {

    /**
     * 获取排除的列
     * @return
     */
    List<String> getExcludeColumn();
}
