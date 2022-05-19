package cn.asany.email.mailbox.graphql;

import cn.asany.email.domainlist.bean.JamesDomain;
import cn.asany.email.domainlist.service.DomainService;
import cn.asany.email.mailbox.bean.JamesMailbox;
import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import cn.asany.email.mailbox.bean.toys.MailboxIdUidKey;
import cn.asany.email.mailbox.component.JPAId;
import cn.asany.email.mailbox.convert.MailboxMessageConverter;
import cn.asany.email.mailbox.graphql.input.MailboxMessageCreateInput;
import cn.asany.email.mailbox.graphql.input.MailboxMessageFilter;
import cn.asany.email.mailbox.graphql.input.MailboxMessageUpdateInput;
import cn.asany.email.mailbox.graphql.type.MailboxMessageConnection;
import cn.asany.email.mailbox.graphql.type.MailboxMessageResult;
import cn.asany.email.mailbox.service.MailboxMessageService;
import cn.asany.email.mailbox.service.MailboxService;
import cn.asany.email.utils.JamesUtil;
import cn.asany.email.utils.SentMailContext;
import cn.asany.security.core.exception.ValidDataException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import lombok.SneakyThrows;
import org.apache.james.core.MaybeSender;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.MessageManager;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.model.Mailbox;
import org.apache.james.mailbox.model.MessageMetaData;
import org.apache.james.mailbox.model.MessageRange;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mailetcontainer.api.MailProcessor;
import org.apache.james.server.core.MailImpl;
import org.apache.james.server.core.MimeMessageCopyOnWriteProxy;
import org.apache.james.server.core.MimeMessageInputStreamSource;
import org.apache.mailet.Mail;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MailboxMessageGraphqlApiResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final DomainService domainService;
  private final MailboxService mailboxService;
  private final MailboxMessageService mailboxMessageService;
  public final MailboxMessageConverter mailboxMessageConverter;
  private final MailProcessor mailProcessor;

  public MailboxMessageGraphqlApiResolver(
      DomainService domainService,
      MailboxService mailboxService,
      MailboxManager mailboxManager,
      MailProcessor mailProcessor,
      MailboxMessageService mailboxMessageService,
      MailboxMessageConverter mailboxMessageConverter) {
    this.domainService = domainService;
    this.mailboxService = mailboxService;
    this.mailProcessor = mailProcessor;
    this.mailboxMessageService = mailboxMessageService;
    this.mailboxMessageConverter = mailboxMessageConverter;
  }

  public MailboxMessageResult mailboxMessage(String id, DataFetchingEnvironment environment) {
    Optional<JamesMailboxMessage> messageOptional =
        this.mailboxMessageService.findMailboxMessageById(new MailboxIdUidKey(id));
    if (!messageOptional.isPresent()) {
      throw new NotFoundException("查询的邮件不存在");
    }
    return JamesUtil.wrap(messageOptional.get());
  }

  public MailboxMessageConnection mailboxMessages(
      String account,
      MailboxMessageFilter filter,
      int offset,
      int first,
      int last,
      String after,
      String before,
      int page,
      int pageSize,
      OrderBy orderBy) {

    JamesDomain domain = domainService.getDefaultDomain();
    LoginUser user = SpringSecurityUtils.getCurrentUser();

    orderBy = ObjectUtil.defaultValue(orderBy, () -> OrderBy.desc("internalDate"));

    String mailUserId = StringUtil.isBlank(account) ? JamesUtil.getUserName(user) : account;

    Pageable pageable;

    if (first > 0) {
      pageable = PageRequest.of(offset, first, orderBy.toSort());
    } else {
      pageable = PageRequest.of(page, pageSize, orderBy.toSort());
    }

    return Kit.connection(
        this.mailboxMessageService.findPage(pageable, filter.build(mailUserId)),
        MailboxMessageConnection.class,
        (Function<JamesMailboxMessage, Edge>)
            message -> new MailboxMessageConnection.MailboxMessageEdge(JamesUtil.wrap(message)));
  }

  public MailboxMessageResult createMailboxMessage(MailboxMessageCreateInput input, String user)
      throws MailboxException {

    String mailUser =
        StringUtil.defaultValue(
            user, () -> JamesUtil.getUserName(SpringSecurityUtils.getCurrentUser()));

    MailboxSession session = JamesUtil.createSession(mailUser);

    JamesMailboxMessage message = this.mailboxMessageConverter.toMailboxMessage(input, mailUser);

    MessageMapper messageMapper = JamesUtil.createMessageMapper(session);

    Mailbox mailbox = JamesUtil.findMailbox(session, DefaultMailboxes.DRAFTS);

    MessageMetaData metaData = messageMapper.add(mailbox, message);

    assert mailbox != null;
    JPAId mailboxId = (JPAId) mailbox.getMailboxId();

    return JamesUtil.wrap(
        this.mailboxMessageService
            .findMailboxMessageById(
                new MailboxIdUidKey(mailboxId.getRawId(), metaData.getUid().asLong()))
            .orElseThrow(() -> new ValidDataException("新增失败")));
  }

  public MailboxMessageResult updateMailboxMessage(
      String id, MailboxMessageUpdateInput input, Boolean merge) {

    Optional<JamesMailboxMessage> optionalMessage =
        this.mailboxMessageService.findMailboxMessageById(new MailboxIdUidKey(id));
    JamesMailboxMessage message = optionalMessage.orElseThrow(() -> new NotFoundException("邮件不存在"));

    message = this.mailboxMessageConverter.toMailboxMessage(input, message);

    return JamesUtil.wrap(this.mailboxMessageService.update(id, message, merge));
  }

  public Boolean deleteMailboxMessage(String id) throws MailboxException {
    Optional<JamesMailboxMessage> optional =
        this.mailboxMessageService.findMailboxMessageById(new MailboxIdUidKey(id));

    JamesMailboxMessage message = optional.orElseThrow(() -> new NotFoundException("邮件不存在"));

    String mailUser = message.getMailbox().getUser();

    MailboxSession session = JamesUtil.createSession(mailUser);

    MessageMapper messageMapper = JamesUtil.createMessageMapper(session);
    MailboxMapper mailboxMapper = JamesUtil.createMailboxMapper(session);

    messageMapper.delete(message.getMailbox().toMailbox(), message);
    return Boolean.TRUE;
  }

  public int clearMailboxMessagesInTrashMailbox(String user) throws MailboxException {
    MailboxSession session = JamesUtil.createSession(user, SpringSecurityUtils.getCurrentUser());

    Mailbox mailbox = JamesUtil.findMailbox(session, DefaultMailboxes.TRASH);
    JPAId mailboxId = (JPAId) mailbox.getMailboxId();

    return this.mailboxMessageService.deleteDeletedMessagesInMailbox(mailboxId.getRawId());
  }

  public MailboxMessageResult sendMailboxMessage(String id)
      throws MailboxException, IOException, MessagingException {

    Optional<JamesMailboxMessage> optional =
        this.mailboxMessageService.findMailboxMessageById(new MailboxIdUidKey(id));

    JamesMailboxMessage draft = optional.orElseThrow(() -> new NotFoundException("邮件不存在"));

    if (!draft.isDraft()) {
      throw new ValidationException("该邮件不是草稿");
    }

    JamesMailboxMessage message = this.mailboxMessageConverter.copyMailboxMessage(draft);

    String mailUser = draft.getMailbox().getUser();

    MailboxSession session = JamesUtil.createSession(mailUser);

    MailboxMessageResult result = JamesUtil.wrap(message);

    Mail mail =
        MailImpl.builder()
            .name(MailImpl.getId())
            .sender(MaybeSender.getMailSender(mailUser))
            .addRecipients(
                result.getTo().flatten().stream()
                    .map(item -> MaybeSender.getMailSender(item.getAddress()).get())
                    .collect(Collectors.toList()))
            .build();

    mail.setState(Mail.DEFAULT);

    MimeMessageInputStreamSource mmiss =
        new MimeMessageInputStreamSource(mail.getName(), message.getFullContent());

    MimeMessageCopyOnWriteProxy mimeMessageCopyOnWriteProxy =
        new MimeMessageCopyOnWriteProxy(mmiss);

    mail.setMessage(mimeMessageCopyOnWriteProxy);

    mailProcessor.service(mail);

    SentMailContext context = SentMailContext.get();

    message = context.getMessage();

    this.deleteMailboxMessage(id);

    return JamesUtil.wrap(message);
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

    messageMapper.updateFlags(mailbox, JamesUtil.convert(flags, mode), range);

    return JamesUtil.wrap(message);
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
    MessageMetaData metaData = messageMapper.move(mailbox, message);

    JPAId jpaId = (JPAId) mailbox.getMailboxId();

    message =
        this.mailboxMessageService.getMailboxMessageById(
            jpaId.getRawId(), metaData.getUid().asLong());

    if (DefaultMailboxes.TRASH.equals(mailbox.getName())) {
      message.setDeleted(true);
      this.mailboxMessageService.update(message);
    }

    return JamesUtil.wrap(message);
  }
}
