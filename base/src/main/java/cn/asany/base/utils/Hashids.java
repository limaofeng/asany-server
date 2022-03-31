package cn.asany.base.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.StringUtil;

public class Hashids {

  private static final String PRIVATE_KEY = StringUtil.md5("asany.cn");

  public static String toId(String str) {
    return Base64.getEncoder()
        .encodeToString(encode(PRIVATE_KEY, str))
        .replaceAll("/", "_")
        .replaceAll("[+]", "-");
  }

  public static String parseId(String key) {
    return new String(
        decode(
            PRIVATE_KEY,
            Base64.getDecoder()
                .decode(
                    key.replaceAll("_", "/")
                        .replaceAll("-", "+")
                        .getBytes(StandardCharsets.UTF_8))),
        StandardCharsets.UTF_8);
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
}
