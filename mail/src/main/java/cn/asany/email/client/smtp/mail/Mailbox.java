package cn.asany.email.client.smtp.mail;

import cn.asany.email.client.smtp.misc.Utils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Mailbox {

  @Nullable private String displayName;
  private String mailbox;
  private String name;
  private String host;

  Mailbox(String mailbox, @Nullable String displayName, String name, String host) {
    this.mailbox = mailbox;
    this.name = name;
    this.host = host;
    this.displayName = displayName;
  }

  @Nonnull
  public String name() {
    return name;
  }

  @Nonnull
  public String host() {
    return host;
  }

  @Nonnull
  public String mailbox() {
    return mailbox;
  }

  @Nonnull
  public String displayName() {
    if (Utils.isBlank(displayName)) return name;
    return displayName;
  }

  @Override
  public String toString() {
    return mailbox;
  }

  @Override
  public int hashCode() {
    return mailbox.hashCode();
  }

  public String canonicalAddress() {
    return "<" + name + "@" + host + ">";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Mailbox) {
      return ((Mailbox) obj).mailbox.equals(mailbox);
    }
    return false;
  }

  /**
   * static factory method to create {@link Mailbox} mailbox. supported three formats below:
   *
   * <p>alice@host.com <alice@host.com> Alice<alice@host.com> this format has a display name
   *
   * @param string the mailbox in text form, simply like alice&lt alice@host.com&gt
   * @return parsed Mailbox or null if the specified string format is illegal
   */
  @Nullable
  public static Mailbox parse(String string) {
    if (string.indexOf('@') == -1) {
      return null;
    }
    String displayName = null;
    String name;
    String host;
    String trimmed = string.trim();

    if (trimmed.indexOf('<') != -1) {
      displayName = trimmed.substring(0, trimmed.indexOf('<')).trim();
      name = trimmed.substring(trimmed.indexOf('<') + 1, trimmed.indexOf('@'));
      host = trimmed.substring(trimmed.indexOf('@') + 1, trimmed.indexOf('>'));
    } else {
      name = trimmed.substring(0, trimmed.indexOf('@'));
      host = trimmed.substring(trimmed.indexOf('@') + 1);
    }
    return new Mailbox(string, displayName, name, host);
  }
}
