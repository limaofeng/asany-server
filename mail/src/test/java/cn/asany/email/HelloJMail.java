package cn.asany.email;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import lombok.SneakyThrows;
import org.apache.james.mailbox.DefaultMailboxes;
import org.junit.jupiter.api.Test;

public class HelloJMail {

  // 发送邮件
  @Test
  @SneakyThrows
  public void sendMail() {
    // String host = "192.168.1.98"; // 指定的smtp服务器，本机的局域网IP
    String host = "smtp.asany.cn"; // 本机smtp服务器
    // String host = "smtp.163.com"; // 163的smtp服务器
    String from = "limaofeng@asany.cn"; // 邮件发送人的邮件地址
    String to = "253161354@asany.cn"; // 邮件接收人的邮件地址
    final String username = "limaofeng@asany.cn"; // 发件人的邮件帐户
    final String password = "rzqabjaiomeuicgd"; // 发件人的邮件密码

    // 创建Properties 对象
    Properties props = System.getProperties();

    // 添加smtp服务器属性
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.auth", "true");

    props.put("mail.smtp.port", 25);

    // 创建邮件会话
    Session session =
        Session.getDefaultInstance(
            props,
            new Authenticator() {
              @Override
              public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
              }
            });

    try {
      // 定义邮件信息
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from, MimeUtility.encodeText("天上天下", "gb2312", "b")));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      //      message.setSubject(transferChinese("我有自己的邮件服务器了"));
      message.setSubject("I hava my own mail server");
      message.setText("From now, you have your own mail server, congratulation!");

      // 发送消息
      session.getTransport("smtp");
      Transport.send(message);
      // Transport.send(message); //也可以这样创建Transport对象发送
      System.out.println("SendMail Process Over!");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @Test
  // 接受邮件
  @SneakyThrows
  public void getMail() {
    String host = "imap.asany.cn";
    final String username = "limaofeng@asany.cn";
    final String password = "rzqabjaiomeuicgd";

    // 创建Properties 对象
    Properties props = new Properties();

    props.put("mail.imap.port", 143);
    //    props.put("mail.imap.starttls.enable", "false");

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

    try {
      // 获取邮箱的pop3存储
      Store store = session.getStore("imap");
      store.connect(host, username, password);

      // 获取inbox文件
      Folder folder = store.getFolder(DefaultMailboxes.OUTBOX); // "#private.Outbox"
      folder.open(Folder.READ_ONLY); // 打开，打开后才能读取邮件信息

      System.out.println("UnreadMessageCount:" + folder.getUnreadMessageCount());

      System.out.println("MessageCount:" + folder.getMessageCount());

      // 获取邮件消息
      Message[] message = folder.getMessages();

      for (int i = 0, n = message.length; i < n; i++) {
        System.out.println(i + ": " + message[i].getFrom()[0] + "\t" + message[i].getSubject());
        try {
          message[i].writeTo(System.out);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      // 关闭资源
      folder.close(false);
      store.close();

    } catch (MessagingException e) {
      e.printStackTrace();
    }

    System.out.println("GetMail Process Over!");
  }

  // 邮件主题中文字符转换
  public static String transferChinese(String strText) {
    try {
      strText = MimeUtility.encodeText(strText);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return strText;
  }
}
