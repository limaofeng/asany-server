package cn.asany.email.quota.component;

import cn.asany.email.quota.domain.JamesCurrentQuota;
import com.google.common.base.Preconditions;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.james.backends.jpa.TransactionRunner;
import org.apache.james.core.quota.QuotaCount;
import org.apache.james.core.quota.QuotaSize;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.model.QuotaRoot;
import org.apache.james.mailbox.store.quota.StoreCurrentQuotaManager;
import org.springframework.stereotype.Component;

@Component
public class JpaCurrentQuotaManager implements StoreCurrentQuotaManager {

  public static final long NO_MESSAGES = 0L;
  public static final long NO_STORED_BYTES = 0L;

  private final EntityManagerFactory entityManagerFactory;
  private final TransactionRunner transactionRunner;

  @Inject
  public JpaCurrentQuotaManager(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
    this.transactionRunner = new TransactionRunner(entityManagerFactory);
  }

  @Override
  public QuotaCount getCurrentMessageCount(QuotaRoot quotaRoot) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    return Optional.ofNullable(retrieveUserQuota(entityManager, quotaRoot))
        .map(JamesCurrentQuota::getMessageCount)
        .orElse(QuotaCount.count(NO_STORED_BYTES));
  }

  @Override
  public QuotaSize getCurrentStorage(QuotaRoot quotaRoot) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    return Optional.ofNullable(retrieveUserQuota(entityManager, quotaRoot))
        .map(JamesCurrentQuota::getSize)
        .orElse(QuotaSize.size(NO_STORED_BYTES));
  }

  @Override
  public void increase(QuotaRoot quotaRoot, long count, long size) {
    Preconditions.checkArgument(count > 0, "Counts should be positive");
    Preconditions.checkArgument(size > 0, "Size should be positive");

    transactionRunner.run(
        entityManager -> {
          JamesCurrentQuota jpaCurrentQuota =
              Optional.ofNullable(retrieveUserQuota(entityManager, quotaRoot))
                  .orElse(
                      new JamesCurrentQuota(quotaRoot.getValue(), NO_MESSAGES, NO_STORED_BYTES));

          entityManager.merge(
              new JamesCurrentQuota(
                  quotaRoot.getValue(),
                  jpaCurrentQuota.getMessageCount().asLong() + count,
                  jpaCurrentQuota.getSize().asLong() + size));
        });
  }

  @Override
  public void decrease(QuotaRoot quotaRoot, long count, long size) throws MailboxException {
    Preconditions.checkArgument(count > 0, "Counts should be positive");
    Preconditions.checkArgument(size > 0, "Counts should be positive");

    transactionRunner.run(
        entityManager -> {
          JamesCurrentQuota jpaCurrentQuota =
              Optional.ofNullable(retrieveUserQuota(entityManager, quotaRoot))
                  .orElse(
                      new JamesCurrentQuota(quotaRoot.getValue(), NO_MESSAGES, NO_STORED_BYTES));

          entityManager.merge(
              new JamesCurrentQuota(
                  quotaRoot.getValue(),
                  jpaCurrentQuota.getMessageCount().asLong() - count,
                  jpaCurrentQuota.getSize().asLong() - size));
        });
  }

  private JamesCurrentQuota retrieveUserQuota(EntityManager entityManager, QuotaRoot quotaRoot) {
    return entityManager.find(JamesCurrentQuota.class, quotaRoot.getValue());
  }
}
