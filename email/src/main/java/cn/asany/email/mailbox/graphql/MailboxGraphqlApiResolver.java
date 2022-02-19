package cn.asany.email.mailbox.graphql;

import cn.asany.email.domainlist.bean.JamesDomain;
import cn.asany.email.domainlist.service.DomainService;
import cn.asany.email.mailbox.bean.JamesMailbox;
import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import cn.asany.email.mailbox.bean.toys.MailboxIdUidKey;
import cn.asany.email.mailbox.graphql.type.MailboxMessageResult;
import cn.asany.email.mailbox.service.MailboxMessageService;
import cn.asany.email.mailbox.service.MailboxService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.*;
import lombok.SneakyThrows;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mailbox.model.MailboxConstants;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.stereotype.Component;

/**
 * Mailbox API
 *
 * @author limaofeng
 */
@Component
public class MailboxGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final DomainService domainService;
  private final MailboxService mailboxService;
  private final MailboxMessageService mailboxMessageService;
  private final MessageBuilder messageBuilder;

  private static final Map<String, String> DEFAULT_MAILBOXES = new HashMap<>();

  static {
    DEFAULT_MAILBOXES.put(DefaultMailboxes.INBOX.toLowerCase(), DefaultMailboxes.INBOX);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.SENT.toLowerCase(), DefaultMailboxes.SENT);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.DRAFTS.toLowerCase(), DefaultMailboxes.DRAFTS);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.SPAM.toLowerCase(), DefaultMailboxes.SPAM);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.ARCHIVE.toLowerCase(), DefaultMailboxes.ARCHIVE);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.OUTBOX.toLowerCase(), DefaultMailboxes.OUTBOX);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.TRASH.toLowerCase(), DefaultMailboxes.TRASH);
  }

  public MailboxGraphqlApiResolver(
      DomainService domainService,
      MailboxService mailboxService,
      MailboxMessageService mailboxMessageService,
      MessageBuilder messageBuilder) {
    this.domainService = domainService;
    this.mailboxService = mailboxService;
    this.mailboxMessageService = mailboxMessageService;
    this.messageBuilder = messageBuilder;
  }

  public List<JamesMailbox> mailboxes() {
    JamesDomain domain = domainService.getDefaultDomain();
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    String mailUserId = user.getUsername() + '@' + domain.getName();
    return this.mailboxService.findMailboxesWithUser(mailUserId, MailboxConstants.USER_NAMESPACE);
  }

  public MailboxMessageResult mailboxMessage(String id) {
    Optional<JamesMailboxMessage> mailboxMessageOptional =
        this.mailboxService.findMailboxMessageById(new MailboxIdUidKey(id));
    if (!mailboxMessageOptional.isPresent()) {
      throw new NotFoundException("查询的邮件不存在");
    }
    return wrap(mailboxMessageOptional.get());
  }

  public List<MailboxMessageResult> mailboxMessages(String mailbox, Integer size, String account) {
    JamesDomain domain = domainService.getDefaultDomain();
    LoginUser user = SpringSecurityUtils.getCurrentUser();

    String mailUserId;
    if (StringUtil.isBlank(account)) {
      mailUserId = user.getUsername() + '@' + domain.getName();
    } else {
      mailUserId = account;
    }

    // TODO: 判断当前用户是否有权限访问该邮件账户

    Long mailboxId;
    if (!RegexpUtil.isMatch(mailbox, RegexpConstant.VALIDATOR_INTEGE)) {
      Optional<JamesMailbox> optionalMailbox =
          this.mailboxService.findMailboxByNameWithUser(
              DEFAULT_MAILBOXES.get(mailbox), mailUserId, MailboxConstants.USER_NAMESPACE);
      if (!optionalMailbox.isPresent()) {
        throw new ValidationException(mailbox + "不存在");
      }
      mailboxId = optionalMailbox.get().getId();
    } else {
      mailboxId = Long.valueOf(mailbox);
    }
    return wrap(this.mailboxMessageService.findMessagesInMailbox(mailboxId, 100));
  }

  @SneakyThrows
  private MailboxMessageResult wrap(JamesMailboxMessage message) {
    return MailboxMessageResult.builder()
        .mailboxMessage(message)
        .message(messageBuilder.parseMessage(message.getFullContent()))
        .build();
  }

  private List<MailboxMessageResult> wrap(List<JamesMailboxMessage> messages) {
    List<MailboxMessageResult> results = new ArrayList<>();
    for (JamesMailboxMessage message : messages) {
      results.add(wrap(message));
    }
    return results;
  }
}
