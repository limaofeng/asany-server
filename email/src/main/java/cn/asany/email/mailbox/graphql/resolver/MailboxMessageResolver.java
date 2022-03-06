package cn.asany.email.mailbox.graphql.resolver;

import cn.asany.email.mailbox.graphql.type.MailboxMessageResult;
import cn.asany.email.mailbox.service.MailboxMessageService;
import graphql.kickstart.tools.GraphQLResolver;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.bouncycastle.util.encoders.Base64;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
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

  public String toMailString(Mailbox mailbox) {
    if (StringUtil.isBlank(mailbox.getName()) || mailbox.getLocalPart().equals(mailbox.getName())) {
      return mailbox.getAddress();
    }
    return mailbox.getName() + "<" + mailbox.getAddress() + ">";
  }

  public List<String> from(MailboxMessageResult result) {
    return result.getFrom().stream().map(this::toMailString).collect(Collectors.toList());
  }

  public List<String> to(MailboxMessageResult result) {
    if (result.getTo() == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(result.getTo().flatten())
        .stream().map(this::toMailString).collect(Collectors.toList());
  }

  public List<String> cc(MailboxMessageResult result) {
    if (result.getCc() == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(result.getCc().flatten())
        .stream().map(this::toMailString).collect(Collectors.toList());
  }

  public List<String> bcc(MailboxMessageResult result) {
    if (result.getBcc() == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(result.getBcc().flatten())
        .stream().map(this::toMailString).collect(Collectors.toList());
  }

  public List<String> replyTo(MailboxMessageResult result) {
    if (result.getReplyTo() == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(result.getReplyTo().flatten())
        .stream().map(this::toMailString).collect(Collectors.toList());
  }

  public String mimeType(MailboxMessageResult result) {
    return result.getMimeType();
  }

  public long index(MailboxMessageResult result) {
    return this.mailboxMessageService.index(
        result.getMailboxMessage().getId(), result.getMailboxMessage().getMailboxId().getRawId());
  }

  public String mailboxName(MailboxMessageResult result) {
    return result.getMailbox().getName();
  }

  public String originalMailboxName(MailboxMessageResult result) {
    String mailboxName = result.getMailbox().getName();
    String user = result.getMailbox().getUser();
    if (DefaultMailboxes.DRAFTS.equals(mailboxName)) {
      return mailboxName;
    }
    if (ObjectUtil.exists(this.from(result), user)) {
      return DefaultMailboxes.SENT;
    }
    return DefaultMailboxes.INBOX;
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
