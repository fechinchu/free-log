package cn.zhuguoqing.operationLog.support.service.impl;

import cn.zhuguoqing.operationLog.support.service.IExcludeColumnGetService;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/3 15:16
 * @Desription: 可以默认实现,通过AutoConfiguration来依赖注入我们的自定义的类,这里就不麻烦了;
 * @Version: 1.0
 */

public class DefaultExcludeColumnGetServiceImpl implements IExcludeColumnGetService {


    @Override
    public List<String> getExcludeColumn() {
        List<String> exclude = Lists.newArrayList();
        exclude.add("id");
        exclude.add("is_deleted");
        exclude.add("create_userid");
        exclude.add("create_username");
        exclude.add("create_userip");
        exclude.add("create_time");
        exclude.add("update_userid");
        exclude.add("update_username");
        exclude.add("update_userip");
        exclude.add("update_time");
        exclude.add("create_user_id");
        exclude.add("create_user_name");
        exclude.add("create_user_ip");
        exclude.add("update_user_id");
        exclude.add("update_user_name");
        exclude.add("update_user_ip");
        exclude.add("version_no");
        return exclude;
    }
}
