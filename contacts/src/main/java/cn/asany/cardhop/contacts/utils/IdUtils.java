package cn.asany.cardhop.contacts.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import lombok.*;
import org.jfantasy.framework.util.common.StringUtil;

public class IdUtils {

  private static final String PRIVATE_KEY = StringUtil.md5("asany.cn");

  public static String toKey(Long book, String namespace, Long group) {
    String key = book + "." + namespace + "." + group;
    return Base64.getEncoder().encodeToString(encode(PRIVATE_KEY, key)).replaceAll("/", "-");
  }

  public static IdKey parseKey(String key) {
    String out =
        new String(
            decode(
                PRIVATE_KEY,
                Base64.getDecoder()
                    .decode(key.replaceAll("-", "/").getBytes(StandardCharsets.UTF_8))),
            StandardCharsets.UTF_8);
    String[] ids = StringUtil.tokenizeToStringArray(out, ".");
    IdKey.IdKeyBuilder builder =
        IdKey.builder()
            .book(Long.parseLong(ids[0]))
            .namespace(ids[1])
            .group(Long.parseLong(ids[2]));
    if (ids.length > 3) {
      builder.book(Long.parseLong(ids[4]));
    }
    return builder.build();
  }

  @SneakyThrows
  public static Key desKey(String _key) {
    byte[] key = _key.getBytes(StandardCharsets.UTF_8);
    DESedeKeySpec spec = new DESedeKeySpec(key);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
    return keyFactory.generateSecret(spec);
  }

  @SneakyThrows
  public static byte[] decode(String _key, byte[] data) {
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, desKey(_key));
    return cipher.doFinal(data);
  }

  @SneakyThrows
  public static byte[] encode(String _key, String data) {
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, desKey(_key));
    return cipher.doFinal(data.getBytes());
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
