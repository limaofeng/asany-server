package cn.asany.email.mailbox.service;

import cn.asany.email.mailbox.bean.AbstractJPAMailboxMessage;
import cn.asany.email.mailbox.bean.AbstractJPAMailboxMessage.MailboxIdUidKey;
import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import cn.asany.email.mailbox.bean.JamesProperty;
import cn.asany.email.mailbox.bean.JamesUserFlag;
import cn.asany.email.mailbox.dao.MailboxMessageDao;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.james.mailbox.MessageUid;
import org.apache.james.mailbox.store.mail.model.Property;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MailboxMessageService {

  private final MailboxMessageDao mailboxMessageDao;

  public MailboxMessageService(MailboxMessageDao mailboxMessageDao) {
    this.mailboxMessageDao = mailboxMessageDao;
  }

  public void deleteMessages(MailboxIdUidKey... ids) {
    this.mailboxMessageDao.deleteAllById(Arrays.asList(ids));
  }

  public long countMessagesInMailbox(long mailbox) {
    return this.mailboxMessageDao.count(
        PropertyFilter.builder().equal("mailbox.id", mailbox).build());
  }

  public long countUnseenMessagesInMailbox(long mailbox) {
    return this.mailboxMessageDao.count(
        PropertyFilter.builder().equal("mailbox.id", mailbox).equal("seen", Boolean.FALSE).build());
  }

  public List<JamesMailboxMessage> findUnseenMessagesInMailboxOrderByUid(long mailbox, int size) {
    return this.mailboxMessageDao
        .findPager(
            new Pager<>(1, size, OrderBy.asc("id")),
            PropertyFilter.builder()
                .equal("mailbox.id", mailbox)
                .equal("seen", Boolean.FALSE)
                .build())
        .getPageItems();
  }

  public List<MessageUid> findRecentMessageUidsInMailbox(long mailbox) {
    return this.mailboxMessageDao
        .findAll(
            PropertyFilter.builder()
                .equal("mailbox.id", mailbox)
                .equal("recent", Boolean.TRUE)
                .build(),
            Sort.by("id").ascending())
        .stream()
        .map(AbstractJPAMailboxMessage::getUid)
        .collect(Collectors.toList());
  }

  public void save(JamesMailboxMessage message) {
    for (Property property : message.getProperties()) {
      JamesProperty jpaProperty = (JamesProperty) property;
      jpaProperty.setUid(message.getUid().asLong());
      jpaProperty.setMailboxId(message.getMailboxId().getRawId());
    }
    for (JamesUserFlag userFlag : message.getUserFlags()) {
      userFlag.setUid(message.getUid().asLong());
      userFlag.setMailboxId(message.getMailboxId().getRawId());
    }
    this.mailboxMessageDao.save(message);
  }

  public List<JamesMailboxMessage> findMessagesInMailboxAfterUID(long mailbox, long uid, int size) {
    List<PropertyFilter> filters =
        PropertyFilter.builder().equal("mailbox.id", mailbox).greaterThanOrEqual("id", uid).build();
    return this.mailboxMessageDao.findAll(filters, size, Sort.by("id").ascending());
  }

  public List<JamesMailboxMessage> findMessagesInMailboxWithUID(long mailbox, long uid, int size) {
    return this.findMessagesInMailboxAfterUID(mailbox, uid, size);
  }

  public List<JamesMailboxMessage> findMessagesInMailboxBetweenUIDs(
      long mailbox, long from, long to, int size) {
    List<PropertyFilter> filters =
        PropertyFilter.builder().equal("mailbox.id", mailbox).between("id", from, to).build();
    return this.mailboxMessageDao.findAll(filters, size, Sort.by("id").ascending());
  }

  public List<JamesMailboxMessage> findMessagesInMailbox(long mailbox, int size) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder().equal("mailbox.id", mailbox).build(),
        size,
        Sort.by("id").ascending());
  }

  public int deleteDeletedMessagesInMailbox(long mailbox) {
    return this.mailboxMessageDao.deleteDeletedMessagesInMailbox(mailbox);
  }

  public int deleteDeletedMessagesInMailboxAfterUID(long mailbox, long uid) {
    return this.mailboxMessageDao.deleteDeletedMessagesInMailboxAfterUID(mailbox, uid);
  }

  public int deleteDeletedMessagesInMailboxWithUID(long mailboxId, long uid) {
    return this.mailboxMessageDao.deleteDeletedMessagesInMailboxWithUID(mailboxId, uid);
  }

  public int deleteDeletedMessagesInMailboxBetweenUIDs(long mailboxId, long from, long to) {
    return this.mailboxMessageDao.deleteDeletedMessagesInMailboxBetweenUIDs(mailboxId, from, to);
  }

  public List<JamesMailboxMessage> findDeletedMessagesInMailbox(long mailboxId) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .build(),
        Sort.by("id").ascending());
  }

  public List<JamesMailboxMessage> findDeletedMessagesInMailboxAfterUID(long mailboxId, long uid) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .greaterThanOrEqual("id", uid)
            .build(),
        Sort.by("id").ascending());
  }

  public List<JamesMailboxMessage> findDeletedMessagesInMailboxWithUID(long mailboxId, long uid) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .equal("id", uid)
            .build(),
        1,
        Sort.by("id").ascending());
  }

  public List<JamesMailboxMessage> findDeletedMessagesInMailboxBetweenUIDs(
      long mailboxId, long from, long to) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .between("id", from, to)
            .build(),
        Sort.by("id").ascending());
  }
}
