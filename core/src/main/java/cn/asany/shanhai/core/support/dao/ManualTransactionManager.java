package cn.asany.shanhai.core.support.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.orm.hibernate5.SpringSessionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ManualTransactionManager {

    /**
     * TODO 在 bindSession 的同时，应该将 sessionFactory 缓存到上下文对象中，因为 sessionFactory 可能会动态改变。
     * 所以 ModelJpaRepository 的 SessionFactory 对象，应该从 上下文对象中 获取
     */
    private SessionFactory sessionFactory;

    public ManualTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void commitTransaction() {
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
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        session.clear();
        transaction.rollback();
    }

    public void bindSession() {
        Session session = sessionFactory.openSession();
        TransactionSynchronizationManager.initSynchronization();
        SessionHolder sessionHolder = new SessionHolder(session);
        TransactionSynchronizationManager.registerSynchronization(new SpringSessionSynchronization(sessionHolder, sessionFactory, true));
        TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
        sessionHolder.setSynchronizedWithTransaction(true);
    }

    public void beginTransaction() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
    }

    public void unbindSession() {
        Session session = sessionFactory.getCurrentSession();
        TransactionSynchronizationManager.clearSynchronization();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        session.close();
    }

}