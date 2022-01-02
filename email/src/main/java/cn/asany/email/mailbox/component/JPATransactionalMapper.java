package cn.asany.email.mailbox.component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.store.transaction.TransactionalMapper;

public abstract class JPATransactionalMapper extends TransactionalMapper {

  protected EntityManagerFactory entityManagerFactory;
  protected EntityManager entityManager;

  public JPATransactionalMapper(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  /**
   * Return the currently used {@link EntityManager} or a new one if none exists.
   *
   * @return entitymanger
   */
  public EntityManager getEntityManager() {
    if (entityManager != null) {
      return entityManager;
    }
    entityManager = entityManagerFactory.createEntityManager();
    return entityManager;
  }

  @Override
  protected void begin() throws MailboxException {
    try {
      getEntityManager().getTransaction().begin();
    } catch (PersistenceException e) {
      throw new MailboxException("Begin of transaction failed", e);
    }
  }

  /** Commit the Transaction and close the EntityManager */
  @Override
  protected void commit() throws MailboxException {
    try {
      getEntityManager().getTransaction().commit();
    } catch (PersistenceException e) {
      throw new MailboxException("Commit of transaction failed", e);
    }
  }

  @Override
  protected void rollback() throws MailboxException {
    EntityTransaction transaction = entityManager.getTransaction();
    // check if we have a transaction to rollback
    if (transaction.isActive()) {
      getEntityManager().getTransaction().rollback();
    }
  }

  /** Close open {@link EntityManager} */
  @Override
  public void endRequest() {
    if (entityManager != null) {
      if (entityManager.isOpen()) {
        entityManager.close();
      }
      entityManager = null;
    }
  }
}
