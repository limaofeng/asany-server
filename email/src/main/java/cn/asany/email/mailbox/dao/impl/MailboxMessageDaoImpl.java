package cn.asany.email.mailbox.dao.impl;

import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import cn.asany.email.mailbox.bean.toys.MailboxIdUidKey;
import cn.asany.email.mailbox.dao.MailboxMessageDao;
import java.util.List;
import javax.persistence.EntityManager;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.IgnoreException;

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
    throw new IgnoreException(" No SQL ");
  }

  @Override
  public int deleteDeletedMessagesInMailboxAfterUID(long mailbox, long uid) {
    throw new IgnoreException(" No SQL ");
  }

  @Override
  public int deleteDeletedMessagesInMailboxWithUID(long mailboxId, long uid) {
    throw new IgnoreException(" No SQL ");
  }

  @Override
  public int deleteDeletedMessagesInMailboxBetweenUIDs(long mailboxId, long from, long to) {
    throw new IgnoreException(" No SQL ");
  }

  @Override
  public Pager<JamesMailboxMessage> findWithDetailsPager(
      Pager<JamesMailboxMessage> pager, List<PropertyFilter> filters) {
    return this.findPager(pager, filters);
  }
}
