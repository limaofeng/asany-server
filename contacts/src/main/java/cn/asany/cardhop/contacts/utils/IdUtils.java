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
package cn.asany.cardhop.contacts.utils;

import cn.asany.base.utils.Hashids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.util.common.StringUtil;

public class IdUtils {

  private static final String PRIVATE_KEY = StringUtil.md5("asany.cn");

  public static String toKey(Long book, String namespace) {
    String key = book + "." + namespace;
    return Hashids.toId(key);
  }

  public static String toKey(Long book, String namespace, Long group) {
    String key = book + "." + namespace + "." + group;
    return Hashids.toId(key);
  }

  public static String toKey(Long book, String namespace, Long group, Long contact) {
    String key = book + "." + namespace + "." + group + "." + contact;
    return Hashids.toId(key);
  }

  public static IdKey parseKey(String key) {
    String out = Hashids.parseId(key);
    String[] ids = StringUtil.tokenizeToStringArray(out, ".");
    IdKey.IdKeyBuilder builder = IdKey.builder().book(Long.parseLong(ids[0])).namespace(ids[1]);
    if (ids.length > 2 && !"null".equals(ids[2])) {
      builder.group(Long.parseLong(ids[2]));
    }
    if (ids.length > 3) {
      builder.contact(Long.parseLong(ids[3]));
    }
    return builder.build();
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class IdKey {
    private Long book;
    private String namespace;
    private Long group;
    private Long contact;
  }
}
