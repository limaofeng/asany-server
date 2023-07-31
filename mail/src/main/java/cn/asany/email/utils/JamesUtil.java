package cn.asany.email.utils;

import cn.asany.email.domainlist.domain.JamesDomain;
import cn.asany.email.domainlist.service.DomainService;
import cn.asany.email.mailbox.component.JPAMailboxSessionMapperFactory;
import cn.asany.email.mailbox.domain.JamesMailboxMessage;
import cn.asany.email.mailbox.graphql.type.MailboxMessageResult;
import cn.asany.email.user.service.MailUserService;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.service.UserService;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import javax.mail.Flags;
import lombok.SneakyThrows;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.MessageManager;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.model.Mailbox;
import org.apache.james.mailbox.model.MailboxPath;
import org.apache.james.mailbox.store.FlagsUpdateCalculator;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.SessionProvider;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.TextBody;
import org.bouncycastle.util.encoders.Base64;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;

public class JamesUtil {

  private static MessageBuilder MESSAGE_BUILDER;
  private static DomainService DOMAIN_SERVICE;
  private static SessionProvider SESSION_PROVIDER;
  private static MailUserService MAIL_USER_SERVICE;
  private static UserService USER_SERVICE;
  private static MailboxSessionMapperFactory MAILBOX_SESSION_MAPPER_FACTORY;

  public static final Map<String, String> DEFAULT_MAILBOXES = new HashMap<>();
  public static final Map<String, Flags.Flag> DEFAULT_MAIL_FLAGS = new HashMap<>();

  static {
    DEFAULT_MAILBOXES.put(DefaultMailboxes.INBOX.toLowerCase(), DefaultMailboxes.INBOX);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.SENT.toLowerCase(), DefaultMailboxes.SENT);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.DRAFTS.toLowerCase(), DefaultMailboxes.DRAFTS);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.SPAM.toLowerCase(), DefaultMailboxes.SPAM);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.ARCHIVE.toLowerCase(), DefaultMailboxes.ARCHIVE);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.OUTBOX.toLowerCase(), DefaultMailboxes.OUTBOX);
    DEFAULT_MAILBOXES.put(DefaultMailboxes.TRASH.toLowerCase(), DefaultMailboxes.TRASH);

    DEFAULT_MAIL_FLAGS.put("seen", Flags.Flag.SEEN);
    DEFAULT_MAIL_FLAGS.put("answered", Flags.Flag.ANSWERED);
    DEFAULT_MAIL_FLAGS.put("flagged", Flags.Flag.FLAGGED);
    DEFAULT_MAIL_FLAGS.put("deleted", Flags.Flag.DELETED);
    DEFAULT_MAIL_FLAGS.put("draft", Flags.Flag.DRAFT);
    DEFAULT_MAIL_FLAGS.put("recent", Flags.Flag.RECENT);
  }

  @SneakyThrows
  public static MessageMapper createMessageMapper(MailboxSession session) {
    return getMailboxSessionMapperFactory().createMessageMapper(session);
  }

  @SneakyThrows
  public static MailboxMapper createMailboxMapper(MailboxSession session) {

    return getMailboxSessionMapperFactory().createMailboxMapper(session);
  }

  public static MailboxSession createSession(String user, LoginUser loginUser) {
    String mailUserId = StringUtil.isBlank(user) ? JamesUtil.getUserName(loginUser) : user;
    return getSessionProvider().createSystemSession(mailUserId);
  }

  public static MailboxSession createSession(String user) {
    return getSessionProvider().createSystemSession(user);
  }

  public static MailboxSession createSession(LoginUser user) {
    return getSessionProvider().createSystemSession(getUserName(user));
  }

  public static MailUserService mailUserService() {
    if (MAIL_USER_SERVICE == null) {
      return MAIL_USER_SERVICE = SpringBeanUtils.getBean(MailUserService.class);
    }
    return MAIL_USER_SERVICE;
  }

  public static String getUserFullName(String user) throws MailboxException {
    MailUserService mailUserService = mailUserService();
    return mailUserService
        .findById(user)
        .map(
            mailUser ->
                StringUtil.defaultValue(mailUser.getFullName(), () -> mailUser.getUser().getName()))
        .orElse(null);
  }

  public static String getUserNameByUserId(Long loginUserId) {
    Optional<User> optional = getUserService().findById(loginUserId);
    if (!optional.isPresent()) {
      throw new NotFoundException("用户不存在");
    }
    User user = optional.get();
    JamesDomain domain = getDomainService().getDefaultDomain();
    return user.getUsername() + "@" + domain.getName();
  }

  public static String getUserName(LoginUser user) {
    JamesDomain domain = getDomainService().getDefaultDomain();
    return user.getUsername() + '@' + domain.getName();
  }

  private static UserService getUserService() {
    if (USER_SERVICE != null) {
      return USER_SERVICE;
    }
    return USER_SERVICE = SpringBeanUtils.getBean(UserService.class);
  }

  private static DomainService getDomainService() {
    if (DOMAIN_SERVICE != null) {
      return DOMAIN_SERVICE;
    }
    return DOMAIN_SERVICE = SpringBeanUtils.getBean(DomainService.class);
  }

  private static SessionProvider getSessionProvider() {
    if (SESSION_PROVIDER != null) {
      return SESSION_PROVIDER;
    }
    return SESSION_PROVIDER = SpringBeanUtils.getBean(SessionProvider.class);
  }

  private static MailboxSessionMapperFactory getMailboxSessionMapperFactory() {
    if (MAILBOX_SESSION_MAPPER_FACTORY != null) {
      return MAILBOX_SESSION_MAPPER_FACTORY;
    }
    return MAILBOX_SESSION_MAPPER_FACTORY =
        SpringBeanUtils.getBean(JPAMailboxSessionMapperFactory.class);
  }

  public static boolean isName(String mailboxId) {
    return !RegexpUtil.isMatch(mailboxId, RegexpConstant.VALIDATOR_INTEGE);
  }

  public static String parseMailboxName(String mailboxId) {
    if (JamesUtil.DEFAULT_MAILBOXES.containsKey(mailboxId)) {
      return JamesUtil.DEFAULT_MAILBOXES.get(mailboxId);
    }
    return mailboxId;
  }

  public static Mailbox findMailbox(MailboxSession session, String mailboxName)
      throws MailboxException {
    MailboxMapper mailboxMapper = JamesUtil.createMailboxMapper(session);
    return mailboxMapper.findMailboxByPath(
        MailboxPath.forUser(session.getUser().asString(), mailboxName));
  }

  public static FlagsUpdateCalculator convert(
      List<String> flags, MessageManager.FlagsUpdateMode mode) {
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
  public static MailboxMessageResult wrap(JamesMailboxMessage message) {
    return MailboxMessageResult.builder()
        .mailboxMessage(message)
        .message(getMessageBuilder().parseMessage(message.getFullContent()))
        .build();
  }

  public static MessageBuilder getMessageBuilder() {
    if (MESSAGE_BUILDER == null) {
      return MESSAGE_BUILDER = SpringBeanUtils.getBean(MessageBuilder.class);
    }
    return MESSAGE_BUILDER;
  }

  public static List<MailboxMessageResult> wrap(List<JamesMailboxMessage> messages) {
    List<MailboxMessageResult> results = new ArrayList<>();
    for (JamesMailboxMessage message : messages) {
      results.add(wrap(message));
    }
    return results;
  }

  public static String toMailString(org.apache.james.mime4j.dom.address.Mailbox mailbox) {
    if (StringUtil.isBlank(mailbox.getName()) || mailbox.getLocalPart().equals(mailbox.getName())) {
      return mailbox.getAddress();
    }
    return mailbox.getName() + "<" + mailbox.getAddress() + ">";
  }

  public static String bodyTransfer(MailboxMessageResult result) throws IOException {
    if (result.getBody() instanceof TextBody) {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      TextBody body = (TextBody) result.getBody();
      BufferedReader reader = new BufferedReader(body.getReader());
      while (reader.ready()) {
        String line = reader.readLine();
        if (result.isBase64Encoding()) {
          output.write(Base64.decode(line));
        } else {
          output.write(line.getBytes());
        }
      }
      return output.toString();
    }
    return "";
  }
}
