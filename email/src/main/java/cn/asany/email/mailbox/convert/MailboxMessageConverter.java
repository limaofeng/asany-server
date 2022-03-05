package cn.asany.email.mailbox.convert;

import static cn.asany.email.client.smtp.mail.Encoding.AUTO_SELECT;

import cn.asany.email.client.smtp.interceptor.TransferSpec;
import cn.asany.email.client.smtp.mail.Mail;
import cn.asany.email.client.smtp.mail.Mailbox;
import cn.asany.email.client.smtp.mail.TextBody;
import cn.asany.email.client.smtp.misc.Utils;
import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import cn.asany.email.mailbox.graphql.input.MailboxMessageCreateInput;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
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

  @SneakyThrows
  default JamesMailboxMessage toMailboxMessage(MailboxMessageCreateInput input, String mailUser) {
    Mail.Builder builder =
        new Mail.Builder()
            .from(Mailbox.parse(mailUser))
            .recipients(
                input.getTo().stream()
                    .map(Mailbox::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

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

    Utils.MailContent content = Utils.toFullContent(builder.buildNoCheck(), DEFAULT_TRANSFER_SPEC);

    JamesMailboxMessage message = new JamesMailboxMessage(content.getHeader(), content.getBody());

    message.setSeen(true);
    message.setDraft(true);

    return message;
  }
}
