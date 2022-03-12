package cn.asany.email.client.smtp.mail;

import cn.asany.email.client.smtp.auth.Authentication;
import java.util.*;

public final class Mail {

  final Mailbox from;
  final List<Mailbox> recipients;
  final List<Mailbox> cc;
  final List<Mailbox> bcc;
  final Headers headers;
  final MailBody mailBody;
  final Object tag;
  final Authentication auth;

  private Mail(Builder builder) {
    from = builder.from;
    headers = builder.headers.build();
    mailBody = builder.mailBody;
    tag = builder.tag;
    auth = builder.auth;
    recipients = Collections.unmodifiableList(new ArrayList<>(builder.recipients));
    cc = Collections.unmodifiableList(new ArrayList<>(builder.cc));
    bcc = Collections.unmodifiableList(new ArrayList<>(builder.bcc));
  }

  public Mailbox from() {
    return from;
  }

  public List<Mailbox> recipients() {
    return recipients;
  }

  public List<Mailbox> cc() {
    return cc;
  }

  public List<Mailbox> bcc() {
    return bcc;
  }

  public Headers headers() {
    return headers;
  }

  public String subject() {
    return this.headers.get("Subject");
  }

  public MailBody body() {
    return mailBody;
  }

  public Object tag() {
    return tag;
  }

  public Authentication auth() {
    return auth;
  }

  public Builder newBuilder() {
    Builder out = new Builder();
    out.from = from;
    out.recipients.addAll(recipients);
    out.cc.addAll(cc);
    out.bcc.addAll(bcc);
    out.mailBody = mailBody;
    out.headers = headers.newBuilder();
    out.tag = this.tag;
    out.auth = auth;
    return out;
  }

  /** ********************builder************************ */
  public static class Builder {

    private Mailbox from;
    private final HashSet<Mailbox> recipients = new LinkedHashSet<>();
    private final HashSet<Mailbox> cc = new LinkedHashSet<>();
    private final HashSet<Mailbox> bcc = new LinkedHashSet<>();
    private Headers.Builder headers = new Headers.Builder();
    private MailBody mailBody;
    private Object tag;
    private Authentication auth;

    public Builder() {}

    // sender mail address
    public Builder from(Mailbox from) {
      this.from = from;
      return this;
    }

    // receiver mail address
    public Builder addRecipient(Mailbox recipient) {
      recipients.add(recipient);
      return this;
    }

    public Builder recipients(List<Mailbox> recipients) {
      this.recipients.clear();
      this.recipients.addAll(recipients);
      return this;
    }

    // carbon copy mail address(抄送,可以添加多个)
    public Builder addCc(Mailbox value) {
      this.cc.add(value);
      return this;
    }

    public Builder removeCc(Mailbox value) {
      this.cc.remove(value);
      return this;
    }

    public Builder addBcc(Mailbox value) {
      this.bcc.add(value);
      return this;
    }

    public Builder removeBcc(Mailbox value) {
      this.bcc.remove(value);
      return this;
    }

    public Builder replaceHeader(String name, String value) {
      headers.set(name, value);
      return this;
    }

    public Builder addHeader(String name, String value) {
      headers.add(name, value);
      return this;
    }

    public Builder headers(Headers headers) {
      this.headers = headers.newBuilder();
      return this;
    }

    public Builder subject(String subject) {
      this.headers.add("Subject", subject);
      return this;
    }

    public Builder body(MailBody mailBody) {
      this.mailBody = mailBody;
      return this;
    }

    public Builder auth(Authentication auth) {
      this.auth = auth;
      return this;
    }

    public Builder tag(Object tag) {
      this.tag = tag;
      return this;
    }

    public Mail build() {
      if (from == null) throw new IllegalStateException("from == null");
      if (recipients.isEmpty()) throw new IllegalStateException("no recipients");
      final String subject = headers.get("Subject");
      if (subject == null || subject.isEmpty()) throw new IllegalStateException("no subject");
      return new Mail(this);
    }

    public Mail buildNoCheck() {
      return new Mail(this);
    }
  }
}
