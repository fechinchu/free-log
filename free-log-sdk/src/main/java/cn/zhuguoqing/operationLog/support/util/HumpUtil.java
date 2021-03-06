package cn.zhuguoqing.operationLog.support.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guoqing.zhu
 *     <p>description:驼峰命名与下划线命名互转
 */
public class HumpUtil {
  public static final char UNDERLINE = '_';

  /**
   * `userId`转`user_id`
   *
   * @param param 转前值
   * @return 转后值
   */
  public static String camelToUnderline(String param) {
    if (param == null || "".equals(param.trim())) {
      return "";
    }
    int len = param.length();
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      char c = param.charAt(i);
      if (Character.isUpperCase(c)) {
        sb.append(UNDERLINE);
        sb.append(Character.toLowerCase(c));
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * `user_id`转`userId`
   *
   * @param param 转前值
   * @return 转后值
   */
  public static String underlineToCamel(String param) {
    if (param == null || "".equals(param.trim())) {
      return "";
    }
    StringBuilder sb = new StringBuilder(param);
    Matcher mc = Pattern.compile(UNDERLINE + "").matcher(param);
    int i = 0;
    while (mc.find()) {
      int position = mc.end() - (i++);
      String.valueOf(Character.toUpperCase(sb.charAt(position)));
      sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
    }
    return sb.toString();
  }
}
