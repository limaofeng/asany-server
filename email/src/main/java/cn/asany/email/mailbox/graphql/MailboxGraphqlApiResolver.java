package cn.asany.email.mailbox.graphql;

import cn.asany.email.domainlist.bean.JamesDomain;
import cn.asany.email.domainlist.service.DomainService;
import cn.asany.email.mailbox.bean.JamesMailbox;
import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import cn.asany.email.mailbox.service.MailboxMessageService;
import cn.asany.email.mailbox.service.MailboxService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mailbox.model.MailboxConstants;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired private MailboxMessageService mailboxMessageService;

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

  public MailboxGraphqlApiResolver(DomainService domainService, MailboxService mailboxService) {
    this.domainService = domainService;
    this.mailboxService = mailboxService;
  }

  public List<JamesMailbox> mailboxes() {
    JamesDomain domain = domainService.getDefaultDomain();
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    String mailUserId = user.getUsername() + '@' + domain.getName();
    return this.mailboxService.findMailboxesWithUser(mailUserId, MailboxConstants.USER_NAMESPACE);
  }

  public List<JamesMailboxMessage> mailboxMessages(String mailbox) {
    JamesDomain domain = domainService.getDefaultDomain();
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    String mailUserId = user.getUsername() + '@' + domain.getName();

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
    return this.mailboxMessageService.findMessagesInMailbox(mailboxId, 100);
  }
}
