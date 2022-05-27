package cn.asany.email.mailbox.convert;

import static cn.asany.email.client.smtp.mail.Encoding.AUTO_SELECT;

import cn.asany.email.client.smtp.interceptor.TransferSpec;
import cn.asany.email.client.smtp.mail.Encoding;
import cn.asany.email.client.smtp.mail.Mail;
import cn.asany.email.client.smtp.mail.Mailbox;
import cn.asany.email.client.smtp.mail.TextBody;
import cn.asany.email.client.smtp.misc.Utils;
import cn.asany.email.mailbox.domain.JamesMailboxMessage;
import cn.asany.email.mailbox.graphql.input.MailboxMessageCreateInput;
import cn.asany.email.mailbox.graphql.input.MailboxMessageUpdateInput;
import cn.asany.email.mailbox.graphql.type.MailboxMessageResult;
import cn.asany.email.utils.JamesUtil;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.store.mail.model.FlagsFactory;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MailboxMessageConverter {

  TransferSpec DEFAULT_TRANSFER_SPEC =
      new TransferSpec.Builder().charset(Utils.UTF8).encoding(AUTO_SELECT).lengthLimit(76).build();

  static Mail.Builder toMailBuilder(MailboxMessageCreateInput input, String mailUser) {
    Mail.Builder builder = new Mail.Builder().from(Mailbox.parse(mailUser));

    if (ObjectUtil.isNotNull(input.getTo())) {
      builder.recipients(
          input.getTo().stream()
              .map(Mailbox::parse)
              .filter(Objects::nonNull)
              .collect(Collectors.toList()));
    }

    if (ObjectUtil.isNotNull(input.getCc())) {
      for (String mailbox : input.getCc()) {
        builder.addCc(Mailbox.parse(mailbox));
      }
    }

    if (ObjectUtil.isNotNull(input.getBcc())) {
      for (String mailbox : input.getBcc()) {
        builder.addCc(Mailbox.parse(mailbox));
      }
    }

    if (StringUtil.isNotBlank(input.getSubject())) {
      builder.subject(input.getSubject());
    }

    String mimeType = StringUtil.defaultValue(input.getMimeType(), "text/plain");

    Encoding theEncoding = DEFAULT_TRANSFER_SPEC.encoding();

    if (StringUtil.isNotBlank(input.getBody())) {
      builder.body(
          "text/html".equals(mimeType)
              ? TextBody.html(input.getBody())
              : TextBody.plain(input.getBody()));

      if (theEncoding == AUTO_SELECT && StringUtil.isNotBlank(input.getBody())) {
        theEncoding = builder.buildNoCheck().body().transferEncoding();
      }
    }

    builder.addHeader("Content-Transfer-Encoding", theEncoding.encodingName());

    return builder;
  }

  @SneakyThrows
  default JamesMailboxMessage toMailboxMessage(MailboxMessageCreateInput input, String mailUser) {
    Mail.Builder builder = MailboxMessageConverter.toMailBuilder(input, mailUser);
    Utils.MailContent content = Utils.toFullContent(builder.buildNoCheck(), DEFAULT_TRANSFER_SPEC);
    JamesMailboxMessage message = new JamesMailboxMessage(content.getHeader(), content.getBody());
    long headerLength = message.getHeader().length;
    long bodyLength = message.getBody().length;
    message.setBodyStartOctet((int) headerLength);
    message.setTextualLineCount(headerLength + bodyLength);
    if (StringUtil.isNotBlank(input.getMimeType())) {
      String[] mimeTypes = input.getMimeType().split("/");
      message.setMediaType(mimeTypes[0]);
      message.setSubType(mimeTypes[1]);
    }
    message.setSeen(true);
    message.setDraft(true);
    return message;
  }

  @SneakyThrows
  default JamesMailboxMessage toMailboxMessage(
      MailboxMessageUpdateInput input, JamesMailboxMessage preMessage) {
    String mailUser = preMessage.getMailbox().getUser();
    Mail.Builder builder = MailboxMessageConverter.toMailBuilder(input, mailUser);
    Utils.MailContent content = Utils.toFullContent(builder.buildNoCheck(), DEFAULT_TRANSFER_SPEC);
    JamesMailboxMessage message = new JamesMailboxMessage(content.getHeader(), content.getBody());
    long headerLength = message.getHeader().length;
    long bodyLength = message.getBody().length;
    message.setBodyStartOctet((int) headerLength);
    message.setTextualLineCount(headerLength + bodyLength - 3);
    if (StringUtil.isNotBlank(input.getMimeType())) {
      String[] mimeTypes = input.getMimeType().split("/");
      message.setMediaType(mimeTypes[0]);
      message.setSubType(mimeTypes[1]);
    }
    message.setFlags(preMessage.createFlags());
    return message;
  }

  default JamesMailboxMessage copyMailboxMessage(JamesMailboxMessage preMessage)
      throws IOException, MailboxException {
    String mailUser = preMessage.getMailbox().getUser();

    MailboxMessageResult result = JamesUtil.wrap(preMessage);

    String mailUserName = JamesUtil.getUserFullName(mailUser);

    Mail.Builder builder =
        new Mail.Builder()
            .from(Mailbox.parse(mailUserName + "<" + mailUser + ">"))
            .recipients(
                result.getTo().flatten().stream()
                    .map(item -> Mailbox.parse(JamesUtil.toMailString(item)))
                    .collect(Collectors.toList()));

    if (ObjectUtil.isNotNull(result.getCc())) {
      result
          .getCc()
          .flatten()
          .forEach(item -> builder.addCc(Mailbox.parse(JamesUtil.toMailString(item))));
    }

    if (ObjectUtil.isNotNull(result.getBcc())) {
      result
          .getBcc()
          .flatten()
          .forEach(item -> builder.addCc(Mailbox.parse(JamesUtil.toMailString(item))));
    }

    if (StringUtil.isNotBlank(result.getSubject())) {
      builder.subject(result.getSubject());
    }

    Encoding theEncoding = DEFAULT_TRANSFER_SPEC.encoding();

    String mimeType = StringUtil.defaultValue(result.getMimeType(), "text/plain");

    if (result.getBody() != null) {
      String body = JamesUtil.bodyTransfer(result);
      if (StringUtil.isNotBlank(body)) {
        builder.body("text/html".equals(mimeType) ? TextBody.html(body) : TextBody.plain(body));
        if (theEncoding == AUTO_SELECT && StringUtil.isNotBlank(body)) {
          theEncoding = builder.buildNoCheck().body().transferEncoding();
        }
      }
    }

    builder.addHeader("Content-Transfer-Encoding", theEncoding.encodingName());

    Utils.MailContent content = Utils.toFullContent(builder.buildNoCheck(), DEFAULT_TRANSFER_SPEC);
    JamesMailboxMessage message = new JamesMailboxMessage(content.getHeader(), content.getBody());

    long headerLength = message.getHeader().length;
    long bodyLength = message.getBody().length;

    message.setBodyStartOctet((int) headerLength);
    message.setTextualLineCount(headerLength + bodyLength);

    if (StringUtil.isNotBlank(result.getMimeType())) {
      String[] mimeTypes = result.getMimeType().split("/");
      message.setMediaType(mimeTypes[0]);
      message.setSubType(mimeTypes[1]);
    }

    message.setFlags(FlagsFactory.empty());

    return message;
  }
}
