package cn.asany.sms.utils;

import java.util.Map;
import java.util.Set;
import net.asany.jfantasy.framework.util.common.StringUtil;

/** 处理模板内容工具类 */
public class TemplateContentUtil {

  private TemplateContentUtil() {}

  public static String replaceVariables(Map<String, String> params, String content) {

    if (StringUtil.isBlank(content)) {
      return null;
    }

    if (params.size() == 0) {
      return content;
    }

    Set<Map.Entry<String, String>> entries = params.entrySet();
    for (Map.Entry<String, String> entry : entries) {
      String key = entry.getKey();
      String value = entry.getValue();
      content = content.replaceAll("\\{" + key + "\\}", value);
    }

    return content;
  }
}
