package cn.asany.email.client;

import static org.junit.jupiter.api.Assertions.*;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import org.junit.jupiter.api.Test;

class MessageSenderTest {

  private MessageSender messageSender = new MessageSender(null);

  @Test
  void sendMessage() throws MessagingException {
    InternetAddress[] addresses = InternetAddress.parse("李茂峰<limaofeng@asany.cn>");
    String sender = "李茂峰<limaofeng@asany.cn>";
    String recipients = "李茂峰<253161354@qq.com>";
    String subject = "标题";
    //    String content = MimeMessageBuilder.mimeMessageBuilder()
    //        .setSubject("SUBJECT")
    //        .setText("content", "text/plain;").build();
    messageSender.sendMessage(sender, recipients, subject);
  }

  @Test
  void drafts() {
    messageSender.saveDrafts();
  }
}
