package cn.asany.email.mailbox.graphql;

import cn.asany.email.mailbox.component.JPAId;
import cn.asany.email.mailbox.domain.JamesMailbox;
import cn.asany.email.mailbox.service.MailboxService;
import cn.asany.email.utils.JamesUtil;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.model.MailboxConstants;
import org.apache.james.mailbox.model.MailboxId;
import org.apache.james.mailbox.model.MailboxPath;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/**
 * Mailbox API
 *
 * @author limaofeng
 */
@Component
public class MailboxGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final MailboxService mailboxService;
  private final MailboxManager mailboxManager;

  public MailboxGraphqlApiResolver(MailboxService mailboxService, MailboxManager mailboxManager) {
    this.mailboxService = mailboxService;
    this.mailboxManager = mailboxManager;
  }

  public Optional<JamesMailbox> mailbox(String id, String account) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    String mailUser = StringUtil.defaultValue(account, () -> JamesUtil.getUserName(user));
    if (JamesUtil.isName(id)) {
      return this.mailboxService.findMailboxByNameWithUser(
          JamesUtil.parseMailboxName(id), mailUser, MailboxConstants.USER_NAMESPACE);
    } else {
      return this.mailboxService.findMailboxById(Long.parseLong(id));
    }
  }

  public List<JamesMailbox> mailboxes(String account) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    String mailUser = StringUtil.defaultValue(account, () -> JamesUtil.getUserName(user));
    return this.mailboxService.findMailboxesWithUser(mailUser);
  }

  @SneakyThrows(MailboxException.class)
  public JamesMailbox createMailbox(String namespace, String name, String account) {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    String user = StringUtil.defaultValue(account, () -> JamesUtil.getUserName(loginUser));
    MailboxSession session = JamesUtil.createSession(user);
    Optional<MailboxId> mailboxIdOptional =
        mailboxManager.createMailbox(new MailboxPath(namespace, user, name), session);
    assert mailboxIdOptional.isPresent();
    MailboxId mailboxId = mailboxIdOptional.get();
    return this.mailboxService.findMailboxById(((JPAId) mailboxId).getRawId()).orElse(null);
  }
}
