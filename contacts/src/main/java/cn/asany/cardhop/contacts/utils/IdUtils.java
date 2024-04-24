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
