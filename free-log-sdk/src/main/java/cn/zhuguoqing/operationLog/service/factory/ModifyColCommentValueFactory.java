package cn.zhuguoqing.operationLog.service.factory;

import cn.zhuguoqing.operationLog.bean.enums.CustomFunctionType;
import cn.zhuguoqing.operationLog.service.IModifyColCommentValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guoqing.zhu
 *     <p>description:全局自定义方法的工厂
 *
 * @see IModifyColCommentValueService
 */
@Component
public class ModifyColCommentValueFactory {

  /** 关于注释的自定义修改方法 */
  private Map<String, IModifyColCommentValueService> columnCommentFunctions =
      new ConcurrentHashMap<>();

  /** 关于值的自定义修改方法; */
  private Map<String, IModifyColCommentValueService> columnValueFunctions =
      new ConcurrentHashMap<>();

  @Autowired
  public ModifyColCommentValueFactory(List<IModifyColCommentValueService> functions) {
    if (CollectionUtils.isEmpty(functions)) {
      return;
    }
    for (IModifyColCommentValueService service : functions) {
      List<String> names = service.getName();
      if (CollectionUtils.isEmpty(names)) {
        continue;
      }
      if (StringUtils.isEmpty(service.getType())) {
        continue;
      }
      if (service.getType().equals(CustomFunctionType.KEY)) {
        for (String name : names) {
          columnCommentFunctions.put(name, service);
        }

      } else if (service.getType().equals(CustomFunctionType.VALUE)) {
        for (String name : names) {
          columnValueFunctions.put(name, service);
        }
      }
    }
  }

  public IModifyColCommentValueService getService(CustomFunctionType type, String name) {
    if (type == null || StringUtils.isEmpty(name)) {
      return null;
    }
    if (CustomFunctionType.KEY.equals(type)) {
      return columnCommentFunctions.get(name);
    } else if (CustomFunctionType.VALUE.equals(type)) {
      return columnValueFunctions.get(name);
    }
    return null;
  }
}
