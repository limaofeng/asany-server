package cn.asany.email.mailbox.dao.impl;

import cn.asany.email.mailbox.dao.MailboxMessageDao;
import cn.asany.email.mailbox.domain.JamesMailboxMessage;
import cn.asany.email.mailbox.domain.toys.MailboxIdUidKey;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.IgnoreException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class MailboxMessageDaoImpl
    extends ComplexJpaRepository<JamesMailboxMessage, MailboxIdUidKey>
    implements MailboxMessageDao {
  public MailboxMessageDaoImpl(EntityManager entityManager) {
    super(JamesMailboxMessage.class, entityManager);
  }

  @Override
  public int deleteMessages(Long mailboxId) {
    throw new IgnoreException(" No SQL ");
  }

  @Override
  public int deleteDeletedMessagesInMailbox(Long mailbox) {

    Query query =
        this.em.createQuery(
            "DELETE FROM MailUserFlag WHERE mailboxId = :mailboxId and uid in (SELECT message.id FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId AND message.deleted=TRUE) ");
    query.setParameter("mailboxId", mailbox);
    query.executeUpdate();

    query =
        this.em.createQuery(
            "DELETE FROM MailProperty WHERE mailboxId = :mailboxId and uid in (SELECT message.id FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId AND message.deleted=TRUE)");
    query.setParameter("mailboxId", mailbox);
    query.executeUpdate();

    query =
        this.em.createQuery(
            "DELETE FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId AND message.deleted=TRUE");
    query.setParameter("mailboxId", mailbox);
    return query.executeUpdate();
  }

  @Override
  public int deleteDeletedMessagesInMailboxAfterUID(long mailbox, long uid) {
    throw new IgnoreException(" No SQL ");
  }

  @Override
  public int deleteDeletedMessagesInMailboxWithUID(long mailboxId, long uid) {
    String whereSql =
        "message.mailbox.id = :mailboxId AND message.id=:uid AND message.deleted=TRUE";

    Query query =
        this.em.createQuery(
            "DELETE FROM MailUserFlag WHERE mailboxId = :mailboxId and uid in (SELECT message.id FROM MailboxMessage message WHERE "
                + whereSql
                + ") ");
    query.setParameter("mailboxId", mailboxId);
    query.setParameter("uid", uid);
    query.executeUpdate();

    query =
        this.em.createQuery(
            "DELETE FROM MailProperty WHERE mailboxId = :mailboxId and uid in (SELECT message.id FROM MailboxMessage message WHERE "
                + whereSql
                + ")");
    query.setParameter("mailboxId", mailboxId);
    query.setParameter("uid", uid);
    query.executeUpdate();

    query = this.em.createQuery("DELETE FROM MailboxMessage message WHERE " + whereSql);
    query.setParameter("mailboxId", mailboxId);
    query.setParameter("uid", uid);
    return query.executeUpdate();
  }

  @Override
  public int deleteDeletedMessagesInMailboxBetweenUIDs(long mailboxId, long from, long to) {

    String whereSql =
        "message.mailbox.id = :mailboxId AND message.id BETWEEN :from AND :to AND message.deleted=TRUE";

    Query query =
        this.em.createQuery(
            "DELETE FROM MailUserFlag WHERE mailboxId = :mailboxId and uid in (SELECT message.id FROM MailboxMessage message WHERE "
                + whereSql
                + ") ");
    query.setParameter("mailboxId", mailboxId);
    query.setParameter("from", from);
    query.setParameter("to", to);
    query.executeUpdate();

    query =
        this.em.createQuery(
            "DELETE FROM MailProperty WHERE mailboxId = :mailboxId and uid in (SELECT message.id FROM MailboxMessage message WHERE "
                + whereSql
                + ")");
    query.setParameter("mailboxId", mailboxId);
    query.setParameter("from", from);
    query.setParameter("to", to);
    query.executeUpdate();

    query = this.em.createQuery("DELETE FROM MailboxMessage message WHERE " + whereSql);
    query.setParameter("mailboxId", mailboxId);
    query.setParameter("from", from);
    query.setParameter("to", to);
    return query.executeUpdate();
  }

  @Override
  public Page<JamesMailboxMessage> findWithDetailsPage(Pageable pageable, PropertyFilter filter) {
    return this.findPage(pageable, filter);
  }
}
