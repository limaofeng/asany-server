package cn.asany.shanhai.core.support.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Builder
@AllArgsConstructor
public class TransactionStatus {
  private SessionFactory sessionFactory;
  private Transaction transaction;
  private Session session;

  public void rollback() {
    try {
      transaction.rollback();
    } finally {
      unbindSession();
    }
  }

  public void commit() {
    try {
      if (!transaction.isActive()) {
        return;
      }
      transaction.commit();
    } finally {
      unbindSession();
    }
  }

  private void unbindSession() {
    TransactionSynchronizationManager.unbindResource(sessionFactory);
    session.close();
  }

  public static class TransactionStatusBuilder {
    public TransactionStatus build() {
      Session session = sessionFactory.openSession();
      SessionHolder sessionHolder = new SessionHolder(session);
      TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
      sessionHolder.setSynchronizedWithTransaction(true);
      Transaction transaction = session.beginTransaction();
      return new TransactionStatus(this.sessionFactory, transaction, session);
    }
  }
}
