package cn.asany.email.mailbox.component;

import cn.asany.email.mailbox.component.mail.JPAAnnotationMapper;
import cn.asany.email.mailbox.component.mail.JPAMailboxMapper;
import cn.asany.email.mailbox.component.mail.JPAMessageMapper;
import cn.asany.email.mailbox.service.MailboxMessageService;
import cn.asany.email.mailbox.service.MailboxService;
import cn.asany.email.user.component.JPASubscriptionMapper;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.mail.*;
import org.apache.james.mailbox.store.user.SubscriptionMapper;

public class JPAMailboxSessionMapperFactory extends MailboxSessionMapperFactory {

  private final EntityManagerFactory entityManagerFactory;
  private final UidProvider uidProvider;
  private final ModSeqProvider modSeqProvider;

  private MailboxService mailboxService;
  private MailboxMessageService mailboxMessageService;

  @Inject
  public JPAMailboxSessionMapperFactory(
      EntityManagerFactory entityManagerFactory,
      UidProvider uidProvider,
      ModSeqProvider modSeqProvider) {
    this.entityManagerFactory = entityManagerFactory;
    this.uidProvider = uidProvider;
    this.modSeqProvider = modSeqProvider;
    createEntityManager().close();
  }

  @Inject
  public void setMailboxService(MailboxService mailboxService) {
    this.mailboxService = mailboxService;
  }

  @Inject
  public void setMailboxMessageService(MailboxMessageService mailboxMessageService) {
    this.mailboxMessageService = mailboxMessageService;
  }

  @Override
  public MailboxMapper createMailboxMapper(MailboxSession session) {
    // TODO: 检查收件箱配， 如文件夹是否存在
    return new JPAMailboxMapper(entityManagerFactory, mailboxService);
  }

  @Override
  public MessageMapper createMessageMapper(MailboxSession session) {
    // TODO: 检查收件箱配， 如文件夹是否存在
    return new JPAMessageMapper(
        session,
        uidProvider,
        modSeqProvider,
        entityManagerFactory,
        this.mailboxService,
        this.mailboxMessageService);
  }

  @Override
  public MessageIdMapper createMessageIdMapper(MailboxSession session) throws MailboxException {
    throw new NotImplementedException("not implemented");
  }

  @Override
  public SubscriptionMapper createSubscriptionMapper(MailboxSession session) {
    return new JPASubscriptionMapper(entityManagerFactory);
  }

  /**
   * Return a new {@link EntityManager} instance
   *
   * @return manager
   */
  private EntityManager createEntityManager() {
    return entityManagerFactory.createEntityManager();
  }

  @Override
  public AnnotationMapper createAnnotationMapper(MailboxSession session) throws MailboxException {
    return new JPAAnnotationMapper(entityManagerFactory);
  }

  @Override
  public UidProvider getUidProvider() {
    return uidProvider;
  }

  @Override
  public ModSeqProvider getModSeqProvider() {
    return modSeqProvider;
  }
}
