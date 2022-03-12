package cn.asany.email.client;

import static cn.asany.email.client.smtp.mail.Encoding.AUTO_SELECT;

import cn.asany.email.client.imap.ImapClient;
import cn.asany.email.client.smtp.Session;
import cn.asany.email.client.smtp.SmtpClient;
import cn.asany.email.client.smtp.auth.Authentication;
import cn.asany.email.client.smtp.interceptor.TransferSpec;
import cn.asany.email.client.smtp.mail.Mail;
import cn.asany.email.client.smtp.mail.Mailbox;
import cn.asany.email.client.smtp.mail.TextBody;
import cn.asany.email.client.smtp.misc.Utils;
import cn.asany.email.domainlist.service.DomainService;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

  private final DomainService domainService;

  public MessageSender(DomainService domainService) {
    this.domainService = domainService;
  }

  //  public Session createSMTPSession(String from) {
  //    String host = "smtp.asany.cn";
  //    // 创建Properties 对象
  //    Properties props = System.getProperties();
  //    // 添加smtp服务器属性
  //    props.put("mail.smtp.host", host);
  //    props.put("mail.smtp.auth", "true");
  //    props.put("mail.smtp.port", 25);
  //
  //    String username = from;
  //    String password = "";
  //
  //    // 创建邮件会话
  //    return Session.getDefaultInstance(props, new MailAuthenticator(username, password));
  //  }
  //
  //  public Session createIMAPSession() {
  //    return null;
  //  }

  /**
   * 发送邮件
   *
   * <p>// * @param mail 邮件
   */
  @SneakyThrows
  public void sendMessage(String sender, String recipients, String subject) {
    SmtpClient client = new SmtpClient.Builder().defaultPort(25).useStartTls(false).build();

    TextBody body = TextBody.html("<h1>模版</h1>");

    Mail mail =
        new Mail.Builder()
            .from(Mailbox.parse("王文<limaofeng@asany.cn>"))
            .addRecipient(Mailbox.parse("王文<253161354@asany.cn>"))
            .auth(Authentication.of("limaofeng@asany.cn", "rzqabjaiomeuicgd"))
            .subject("第 0 封: 朱自清《春》- 富文本内容")
            .body(body)
            .build();

    Session session = client.newSession(mail, InetAddress.getByName("smtp.asany.cn"));

    session.send();
  }

  /**
   * 保存草稿
   *
   * <p>// * @param mail 邮件
   */
  @SneakyThrows
  void saveDrafts() {
    ImapClient client =
        ImapClient.builder()
            .defaultPort(143)
            .useStartTls(false)
            .host("imap.asany.cn")
            .username("limaofeng@asany.cn")
            .password("rzqabjaiomeuicgd")
            .build();
    TextBody body = TextBody.html("<h1>模版</h1>");
    Mail mail =
        new Mail.Builder()
            .from(Mailbox.parse("王文<limaofeng@msn.com>"))
            .addRecipient(Mailbox.parse("王文<253161354@qq.com>"))
            .auth(Authentication.of("", "1234567"))
            .subject("第 0 封: 朱自清《春》- 富文本内容")
            .body(body)
            .build();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    Utils.toFullContent(
        mail,
        new TransferSpec.Builder()
            .charset(Utils.UTF8)
            .encoding(AUTO_SELECT)
            .lengthLimit(76)
            .build());

    //    client.writeTo(DefaultMailboxes.DRAFTS, mail);

    //    javax.mail.Session session = client.newSession(mail, InetAddress.getByName(""));

    //      client.connect("imap.asany.cn");
    //
    //      client.login("limaofeng@asany.cn", "rzqabjaiomeuicgd");
    //
    //      client.list();

    //    String host = "imap.asany.cn";
    //    final String username = "limaofeng@asany.cn";
    //    final String password = "rzqabjaiomeuicgd";
    //
    //    // 创建Properties 对象
    //    Properties props = new Properties();
    //
    //    props.put("mail.imap.port", 143);
    //    //    props.put("mail.imap.starttls.enable", "false");
    //
    //    Authenticator authenticator =
    //        new Authenticator() {
    //          @Override
    //          public PasswordAuthentication getPasswordAuthentication() {
    //            return new PasswordAuthentication(username, password);
    //          }
    //        };
    //    // 创建邮件会话
    //    Session session = Session.getDefaultInstance(props, authenticator);
    //    session.setDebug(true);
    //
    //    Store store = session.getStore("imap");
    //
    //    store.connect(host, username, password);
    //
    //    Folder folder = store.getFolder(DefaultMailboxes.DRAFTS); // 打开草稿箱
    //
    //    MimeMessage message = new MimeMessage(session);
    //
    //    message.setFrom(new InternetAddress(username));
    //
    //    message.setRecipient(Message.RecipientType.TO, new InternetAddress("to"));
    //
    //    message.setSubject("title");
    //
    //    Multipart mainPart = new MimeMultipart();
    //
    //    BodyPart html = new MimeBodyPart();
    //
    //    html.setContent("content", "text/html; charset=utf-8");
    //
    //    mainPart.addBodyPart(html);
    //    message.setContent(mainPart);
    //    message.setSentDate(new Date());
    //    message.saveChanges();
    //    message.setFlag(Flags.Flag.DRAFT, true);
    //
    //    MimeMessage[] draftMessages = {message};
    //
    //    System.out.println(message.getSubject());
    //
    //    folder.appendMessages(draftMessages);
    //
    //    folder.close();
    //
    //    System.out.println("保存成功");
  }
}
