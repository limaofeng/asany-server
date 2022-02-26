package cn.asany.email.mailbox.graphql.resolver;

import cn.asany.email.mailbox.graphql.type.MailboxMessageResult;
import cn.asany.email.mailbox.service.MailboxMessageService;
import graphql.kickstart.tools.GraphQLResolver;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

@Component
public class MailboxMessageResolver implements GraphQLResolver<MailboxMessageResult> {

  private final MailboxMessageService mailboxMessageService;

  public MailboxMessageResolver(MailboxMessageService mailboxMessageService) {
    this.mailboxMessageService = mailboxMessageService;
  }

  public String id(MailboxMessageResult result) {
    return result.getMailboxMessage().getKey().toKey();
  }

  public List<Mailbox> from(MailboxMessageResult result) {
    return new ArrayList<>(result.getFrom());
  }

  public List<Mailbox> to(MailboxMessageResult result) {
    return new ArrayList<>(result.getTo().flatten());
  }

  public List<Mailbox> cc(MailboxMessageResult result) {
    return new ArrayList<>(result.getCc().flatten());
  }

  public List<Mailbox> bcc(MailboxMessageResult result) {
    return new ArrayList<>(result.getBcc().flatten());
  }

  public List<Mailbox> replyTo(MailboxMessageResult result) {
    return new ArrayList<>(result.getReplyTo().flatten());
  }

  public String mimeType(MailboxMessageResult result) {
    return result.getMimeType();
  }

  public long index(MailboxMessageResult result) {
    return this.mailboxMessageService.index(
        result.getMailboxMessage().getId(), result.getMailboxMessage().getMailboxId().getRawId());
  }

  public String body(MailboxMessageResult mailboxMessageResult) throws IOException {
    if (mailboxMessageResult.getBody() instanceof TextBody) {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      TextBody body = (TextBody) mailboxMessageResult.getBody();
      BufferedReader reader = new BufferedReader(body.getReader());
      while (reader.ready()) {
        String line = reader.readLine();
        if (mailboxMessageResult.isBase64Encoding()) {
          result.write(Base64.decode(line));
        } else {
          result.write(line.getBytes());
        }
      }
      return result.toString();
    }
    return "";
  }
}
