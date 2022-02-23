package cn.asany.email.mailbox.graphql;

import cn.asany.email.domainlist.bean.JamesDomain;
import cn.asany.email.domainlist.service.DomainService;
import cn.asany.email.mailbox.bean.JamesMailbox;
import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import cn.asany.email.mailbox.bean.toys.MailboxIdUidKey;
import cn.asany.email.mailbox.component.JPAId;
import cn.asany.email.mailbox.graphql.input.MailboxMessageFilter;
import cn.asany.email.mailbox.graphql.type.MailboxMessageConnection;
import cn.asany.email.mailbox.graphql.type.MailboxMessageResult;
import cn.asany.email.mailbox.service.MailboxMessageService;
import cn.asany.email.mailbox.service.MailboxService;
import cn.asany.email.utils.JamesUtil;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.mail.Flags;
import lombok.SneakyThrows;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.MessageManager;
import org.apache.james.mailbox.model.Mailbox;
import org.apache.james.mailbox.model.MailboxId;
import org.apache.james.mailbox.model.MailboxPath;
import org.apache.james.mailbox.model.MessageRange;
import org.apache.james.mailbox.store.FlagsUpdateCalculator;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.util.Kit;
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
  private final MailboxManager mailboxManager;

  public MailboxGraphqlApiResolver(
      DomainService domainService,
      MailboxService mailboxService,
      MailboxMessageService mailboxMessageService,
      MessageBuilder messageBuilder,
      MailboxManager mailboxManager) {
    this.domainService = domainService;
    this.mailboxService = mailboxService;
    this.mailboxMessageService = mailboxMessageService;
    this.messageBuilder = messageBuilder;
    this.mailboxManager = mailboxManager;
  }

  public List<JamesMailbox> mailboxes(String account) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    String mailUser = StringUtil.defaultValue(account, () -> JamesUtil.getUserName(user));
    return this.mailboxService.findMailboxesWithUser(mailUser);
  }

  public MailboxMessageResult mailboxMessage(String id) {
    Optional<JamesMailboxMessage> messageOptional =
        this.mailboxMessageService.findMailboxMessageById(new MailboxIdUidKey(id));
    if (!messageOptional.isPresent()) {
      throw new NotFoundException("查询的邮件不存在");
    }
    return wrap(messageOptional.get());
  }

  public MailboxMessageConnection mailboxMessages(
      String account, MailboxMessageFilter filter, int page, int pageSize, OrderBy orderBy) {
    JamesDomain domain = domainService.getDefaultDomain();
    LoginUser user = SpringSecurityUtils.getCurrentUser();

    orderBy = ObjectUtil.defaultValue(orderBy, () -> OrderBy.desc("id"));

    String mailUserId = StringUtil.isBlank(account) ? JamesUtil.getUserName(user) : account;

    // TODO: 应该在 fantasy 框架中实现，完善框架后，需要去除
    //    filter.getBuilder().equal("deleted", Boolean.FALSE);

    // TODO: 判断当前用户是否有权限访问该邮件账户

    Pager<JamesMailboxMessage> pager =
        this.mailboxMessageService.findPager(
            Pager.newPager(page, pageSize, orderBy), filter.build(mailUserId));
    return Kit.connection(
        pager,
        MailboxMessageConnection.class,
        (Function<JamesMailboxMessage, Edge>)
            message -> new MailboxMessageConnection.MailboxMessageEdge(wrap(message)));
  }

  @SneakyThrows
  public MailboxMessageResult updateMailboxMessageFlags(
      String id, List<String> flags, MessageManager.FlagsUpdateMode mode) {
    Optional<JamesMailboxMessage> messageOptional =
        this.mailboxMessageService.findMailboxMessageById(new MailboxIdUidKey(id));

    assert messageOptional.isPresent();

    JamesMailboxMessage message = messageOptional.get();

    MailboxSession session = JamesUtil.createSession(SpringSecurityUtils.getCurrentUser());

    MessageMapper messageMapper = JamesUtil.createMessageMapper(session);
    MailboxMapper mailboxMapper = JamesUtil.createMailboxMapper(session);

    Mailbox mailbox = mailboxMapper.findMailboxById(message.getMailboxId());
    MessageRange range = MessageRange.one(message.getUid());

    messageMapper.updateFlags(mailbox, convert(flags, mode), range);

    return wrap(message);
  }

  @SneakyThrows
  public MailboxMessageResult moveMailboxMessageToFolder(String id, String mailboxId) {
    MailboxIdUidKey key = new MailboxIdUidKey(id);
    Optional<JamesMailboxMessage> messageOptional =
        this.mailboxMessageService.findMailboxMessageById(new MailboxIdUidKey(id));

    assert messageOptional.isPresent();

    String user = JamesUtil.getUserName(SpringSecurityUtils.getCurrentUser());

    JamesMailboxMessage message = messageOptional.get();

    MailboxSession session = JamesUtil.createSession(SpringSecurityUtils.getCurrentUser());

    MessageMapper messageMapper = JamesUtil.createMessageMapper(session);
    MailboxMapper mailboxMapper = JamesUtil.createMailboxMapper(session);

    Optional<JamesMailbox> mailboxOptional =
        JamesUtil.isName(mailboxId)
            ? this.mailboxService.findMailboxByNameWithUser(
                JamesUtil.parseMailboxName(mailboxId), user)
            : this.mailboxService.findMailboxById(Long.parseLong(mailboxId));

    assert mailboxOptional.isPresent();

    Mailbox mailbox = mailboxOptional.get().toMailbox();
    messageMapper.move(mailbox, message);

    return wrap(message);
  }

  @SneakyThrows
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

  private FlagsUpdateCalculator convert(List<String> flags, MessageManager.FlagsUpdateMode mode) {
    Flags _flags = new Flags();
    for (String f : flags) {
      if (JamesUtil.DEFAULT_MAIL_FLAGS.containsKey(f)) {
        _flags.add(JamesUtil.DEFAULT_MAIL_FLAGS.get(f));
      } else {
        _flags.add(f);
      }
    }
    return new FlagsUpdateCalculator(_flags, mode);
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
