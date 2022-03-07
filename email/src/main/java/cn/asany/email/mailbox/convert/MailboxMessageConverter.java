package cn.asany.email.mailbox.convert;

import static cn.asany.email.client.smtp.mail.Encoding.AUTO_SELECT;

import cn.asany.email.client.smtp.interceptor.TransferSpec;
import cn.asany.email.client.smtp.mail.Encoding;
import cn.asany.email.client.smtp.mail.Mail;
import cn.asany.email.client.smtp.mail.Mailbox;
import cn.asany.email.client.smtp.mail.TextBody;
import cn.asany.email.client.smtp.misc.Utils;
import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import cn.asany.email.mailbox.graphql.input.MailboxMessageCreateInput;
import cn.asany.email.mailbox.graphql.input.MailboxMessageUpdateInput;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
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
    Mail.Builder builder =
        new Mail.Builder()
            .from(Mailbox.parse(mailUser))
            .recipients(
                input.getTo().stream()
                    .map(Mailbox::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

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
    if (StringUtil.isNotBlank(input.getBody())) {
      builder.body(
          "text/html".equals(mimeType)
              ? TextBody.html(input.getBody())
              : TextBody.plain(input.getBody()));
    }

    Encoding theEncoding = DEFAULT_TRANSFER_SPEC.encoding();
    if (theEncoding == AUTO_SELECT && StringUtil.isNotBlank(input.getBody())) {
      theEncoding = builder.buildNoCheck().body().transferEncoding();
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
}
