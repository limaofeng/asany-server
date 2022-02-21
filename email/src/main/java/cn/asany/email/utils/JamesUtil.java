package cn.asany.email.utils;

import cn.asany.email.domainlist.bean.JamesDomain;
import cn.asany.email.domainlist.service.DomainService;
import cn.asany.email.mailbox.component.JPAMailboxSessionMapperFactory;
import java.util.HashMap;
import java.util.Map;
import javax.mail.Flags;
import lombok.SneakyThrows;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.SessionProvider;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;

public class JamesUtil {

  private static DomainService DOMAIN_SERVICE;
  private static SessionProvider SESSION_PROVIDER;
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

  public static MailboxSession createSession(LoginUser user) {
    return getSessionProvider().createSystemSession(getUserName(user));
  }

  public static String getUserName(LoginUser user) {
    JamesDomain domain = getDomainService().getDefaultDomain();
    return user.getUsername() + '@' + domain.getName();
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
}
