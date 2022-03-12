package cn.asany.email.client.imap;

import cn.asany.email.client.smtp.mail.Mail;
import cn.asany.email.client.smtp.mail.MailBody;
import cn.asany.email.client.smtp.mail.TextBody;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import org.jfantasy.framework.spring.validation.ValidationException;

@Builder
@AllArgsConstructor
public final class ImapClient {
  private final String host;
  private final int defaultPort;
  private final boolean useStartTls;
  private final String username;
  private final String password;

  private Session session;
  private Store store;

  public Session newSession() throws MessagingException {
    // 创建Properties 对象
    Properties props = new Properties();

    props.put("mail.imap.port", 143);
    Authenticator authenticator =
        new Authenticator() {
          @Override
          public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        };
    // 创建邮件会话
    Session session = Session.getDefaultInstance(props, authenticator);
    session.setDebug(true);

    store = session.getStore("imap");
    store.connect(host, username, password);

    return session;
  }

  @SneakyThrows
  public void writeTo(String mailboxName, Mail mail) {
    if (session == null) {
      this.newSession();
    }
    Folder folder = this.store.getFolder(mailboxName);
    folder.open(Folder.READ_WRITE);
    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(mail.from().canonicalAddress()));
      message.setRecipient(Message.RecipientType.TO, new InternetAddress("to"));
      message.setSubject(mail.subject());
      Multipart mainPart = new MimeMultipart();

      MailBody body = mail.body();

      if (body instanceof TextBody) {
        BodyPart html = new MimeBodyPart();
        TextBody textBody = (TextBody) body;
        html.setContent(
            textBody.getText(), body.contentType().toString()); // "text/html; charset=utf-8"
        mainPart.addBodyPart(html);
      } else {
        throw new ValidationException("未处理的情况");
      }

      message.setContent(mainPart);
      message.setSentDate(new Date());
      message.saveChanges();
      message.setFlag(Flags.Flag.DRAFT, true);
      MimeMessage[] draftMessages = {message};
      System.out.println(message.getSubject());
      folder.appendMessages(draftMessages);
    } finally {
      folder.close(false);
    }
  }

  @SneakyThrows
  public void list(String mailboxName) {
    if (session == null) {
      this.newSession();
    }
    Folder folder = this.store.getFolder(mailboxName);
    folder.open(Folder.READ_ONLY);
    try {
      Message[] message = folder.getMessages();

      for (int i = 0, n = message.length; i < n; i++) {
        System.out.println(i + ": " + message[i].getFrom()[0] + "\t" + message[i].getSubject());
        try {
          message[i].writeTo(System.out);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } finally {
      folder.close(false);
    }
  }

  public void close() throws MessagingException {
    store.close();
  }

  //  public static class ImapClientBuilder {
  //    public ImapClient build() {
  //      int defaultPort = this.defaultPort;
  //      boolean useStartTls = this.useStartTls;
  //      return new ImapClient(defaultPort, useStartTls);
  //    }
  //  }

}
