package cn.asany.email.mailbox.domain.toys;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.persistence.Embeddable;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.StringUtil;

@Embeddable
public class MailboxIdUidKey implements Serializable {

  private static final long serialVersionUID = 7847632032426660997L;

  private static final String PRIVATE_KEY = StringUtil.md5("asany.cn");

  public MailboxIdUidKey() {}

  public MailboxIdUidKey(String key) {
    String out =
        new String(
            decode(
                PRIVATE_KEY,
                Base64.getDecoder()
                    .decode(key.replaceAll("-", "/").getBytes(StandardCharsets.UTF_8))),
            StandardCharsets.UTF_8);
    String[] ids = out.substring(1, out.length() - 1).split(",");
    this.mailbox = Integer.parseInt(ids[0]);
    this.id = Integer.parseInt(ids[1]);
  }

  /** The value for the mailbox field */
  private long mailbox;

  /** The value for the uid field */
  private long id;

  public MailboxIdUidKey(Long mailbox, long id) {
    this.mailbox = mailbox;
    this.id = id;
  }

  public long getMailbox() {
    return mailbox;
  }

  public void setMailbox(long mailbox) {
    this.mailbox = mailbox;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + (int) (mailbox ^ (mailbox >>> 32));
    result = PRIME * result + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MailboxIdUidKey other = (MailboxIdUidKey) obj;
    if (mailbox != other.mailbox) {
      return false;
    }
    return id == other.id;
  }

  @Override
  public String toString() {
    return "MailboxIdUidKey{" + "mailbox=" + mailbox + ", id=" + id + '}';
  }

  public String toKey() {
    return Base64.getEncoder()
        .encodeToString(encode(PRIVATE_KEY, "{" + mailbox + "," + id + "}"))
        .replaceAll("/", "-");
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
