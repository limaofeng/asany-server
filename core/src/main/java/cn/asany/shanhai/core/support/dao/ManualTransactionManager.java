package cn.asany.shanhai.core.support.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 手动事务管理器
 *
 * @author limaofeng
 */
public class ManualTransactionManager {

    private static ThreadLocal<SessionFactory> holder = new ThreadLocal<>();

    private ModelSessionFactory sessionFactory;

    public ManualTransactionManager(ModelSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory buildCurrentSessionFactory() {
        SessionFactory sessionFactoryHolder = holder.get();
        if (sessionFactoryHolder != null) {
            holder.remove();
        }
        holder.set(this.sessionFactory.real());
        return holder.get();
    }

    public void commitTransaction() {
        SessionFactory sessionFactory = getCurrentSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        if (transaction == null) {
            return;
        }
        try {
            if (!transaction.isActive()) {
                return;
            }
            transaction.commit();
        } catch (Exception exception) {
            commitTransaction();
            throw exception;
        }
    }

    public void rollbackTransaction() {
        SessionFactory sessionFactory = getCurrentSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        session.clear();
        transaction.rollback();
    }

    public void bindSession() {
        SessionFactory sessionFactory = this.buildCurrentSessionFactory();
        Session session = sessionFactory.openSession();
        SessionHolder sessionHolder = new SessionHolder(session);
        TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
        sessionHolder.setSynchronizedWithTransaction(true);
    }

    public void beginTransaction() {
        SessionFactory sessionFactory = getCurrentSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
    }

    public static SessionFactory getCurrentSessionFactory() {
        return holder.get();
    }

    public void unbindSession() {
        SessionFactory sessionFactory = getCurrentSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        session.close();
    }

}