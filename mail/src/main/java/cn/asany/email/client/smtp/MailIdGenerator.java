package cn.asany.email.client.smtp;

import cn.asany.email.client.smtp.mail.Mail;

public interface MailIdGenerator {

  MailIdGenerator DEFAULT =
      mail -> {
        // generated message id: timestamp + user + @ + host
        final String timeStamp = String.valueOf(System.nanoTime());
        return timeStamp + mail.from().canonicalAddress();
      };

  String generate(Mail mail);
}
