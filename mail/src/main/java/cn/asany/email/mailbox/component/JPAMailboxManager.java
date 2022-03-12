package cn.asany.email.mailbox.component;

import java.util.EnumSet;
import javax.inject.Inject;
import org.apache.james.mailbox.MailboxPathLocker;
import org.apache.james.mailbox.events.EventBus;
import org.apache.james.mailbox.model.MessageId;
import org.apache.james.mailbox.store.*;
import org.apache.james.mailbox.store.mail.model.impl.MessageParser;
import org.apache.james.mailbox.store.quota.QuotaComponents;
import org.apache.james.mailbox.store.search.MessageSearchIndex;

public class JPAMailboxManager extends StoreMailboxManager {

  public static final EnumSet<MailboxCapabilities> MAILBOX_CAPABILITIES =
      EnumSet.of(
          MailboxCapabilities.UserFlag,
          MailboxCapabilities.Namespace,
          MailboxCapabilities.Move,
          MailboxCapabilities.Annotation);

  @Inject
  public JPAMailboxManager(
      JPAMailboxSessionMapperFactory mapperFactory,
      SessionProvider sessionProvider,
      MessageParser messageParser,
      MessageId.Factory messageIdFactory,
      EventBus eventBus,
      StoreMailboxAnnotationManager annotationManager,
      StoreRightManager storeRightManager,
      QuotaComponents quotaComponents,
      MessageSearchIndex index) {
    this(
        mapperFactory,
        sessionProvider,
        new JVMMailboxPathLocker(),
        messageParser,
        messageIdFactory,
        eventBus,
        annotationManager,
        storeRightManager,
        quotaComponents,
        index);
  }

  private JPAMailboxManager(
      JPAMailboxSessionMapperFactory mailboxSessionMapperFactory,
      SessionProvider sessionProvider,
      MailboxPathLocker locker,
      MessageParser messageParser,
      MessageId.Factory messageIdFactory,
      EventBus eventBus,
      StoreMailboxAnnotationManager annotationManager,
      StoreRightManager storeRightManager,
      QuotaComponents quotaComponents,
      MessageSearchIndex index) {
    super(
        mailboxSessionMapperFactory,
        sessionProvider,
        locker,
        messageParser,
        messageIdFactory,
        annotationManager,
        eventBus,
        storeRightManager,
        quotaComponents,
        index,
        MailboxManagerConfiguration.DEFAULT,
        PreDeletionHooks.NO_PRE_DELETION_HOOK);
  }

  @Override
  public EnumSet<MailboxCapabilities> getSupportedMailboxCapabilities() {
    return MAILBOX_CAPABILITIES;
  }
}
