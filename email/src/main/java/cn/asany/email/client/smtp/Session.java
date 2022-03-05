package cn.asany.email.client.smtp;

import cn.asany.email.client.smtp.mail.Mail;
import java.io.IOException;
import java.net.InetAddress;
import javax.annotation.Nullable;

public interface Session {

  Mail mail();

  SmtpClient client();

  void send() throws IOException;

  Session clone();

  interface SessionFactory {
    Session newSession(Mail mail, @Nullable InetAddress serverAddress);
  }
}
