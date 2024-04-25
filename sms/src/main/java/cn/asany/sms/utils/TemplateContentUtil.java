/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
