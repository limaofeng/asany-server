package cn.asany.email.mailbox.service;

import cn.asany.email.mailbox.bean.*;
import cn.asany.email.mailbox.bean.toys.MailboxIdUidKey;
import cn.asany.email.mailbox.dao.MailboxMessageDao;
import cn.asany.email.utils.SentMailContext;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mailbox.MessageUid;
import org.apache.james.mailbox.store.mail.model.Property;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        .findPage(
            PageRequest.of(1, size, OrderBy.asc("id").toSort()),
            PropertyFilter.builder()
                .equal("mailbox.id", mailbox)
                .equal("seen", Boolean.FALSE)
                .build())
        .getContent();
  }

  public List<MessageUid> findRecentMessageUidsInMailbox(long mailbox) {
    return this.mailboxMessageDao
        .findAll(
            PropertyFilter.builder()
                .equal("mailbox.id", mailbox)
                .equal("recent", Boolean.TRUE)
                .build(),
            Sort.by("id").descending())
        .stream()
        .map(AbstractJPAMailboxMessage::getUid)
        .collect(Collectors.toList());
  }

  public JamesMailboxMessage save(JamesMailboxMessage message) {
    if (DefaultMailboxes.SENT.equals(message.getMailbox().getName())) {
      message.setSeen(true);
      SentMailContext.create(message);
    }
    for (Property property : message.getProperties()) {
      JamesProperty jpaProperty = (JamesProperty) property;
      jpaProperty.setUid(message.getUid().asLong());
      jpaProperty.setMailboxId(message.getMailboxId().getRawId());
    }
    for (JamesUserFlag userFlag : message.getUserFlags()) {
      userFlag.setUid(message.getUid().asLong());
      userFlag.setMailboxId(message.getMailboxId().getRawId());
    }
    return this.mailboxMessageDao.save(message);
  }

  public List<JamesMailboxMessage> findMessagesInMailboxAfterUID(long mailbox, long uid, int size) {
    List<PropertyFilter> filters =
        PropertyFilter.builder().equal("mailbox.id", mailbox).greaterThanOrEqual("id", uid).build();
    return this.mailboxMessageDao.findAll(filters, size, Sort.by("id").descending());
  }

  public List<JamesMailboxMessage> findMessagesInMailboxWithUID(long mailbox, long uid) {
    List<PropertyFilter> filters =
        PropertyFilter.builder().equal("mailbox.id", mailbox).equal("id", uid).build();
    return this.mailboxMessageDao.findAll(filters, Sort.by("id").descending());
  }

  public List<JamesMailboxMessage> findMessagesInMailboxBetweenUIDs(
      long mailbox, long from, long to, int size) {
    List<PropertyFilter> filters =
        PropertyFilter.builder().equal("mailbox.id", mailbox).between("id", from, to).build();
    return this.mailboxMessageDao.findAll(filters, size, Sort.by("id").descending());
  }

  public long index(Date internalDate, long mailbox) {
    return this.mailboxMessageDao.count(
        PropertyFilter.builder()
            .greaterThanOrEqual("internalDate", internalDate)
            .equal("mailbox.id", mailbox)
            .build());
  }

  public List<JamesMailboxMessage> findMessagesInMailbox(long mailbox, int size) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder().equal("mailbox.id", mailbox).build(),
        size,
        Sort.by("id").descending());
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
        Sort.by("id").descending());
  }

  public List<JamesMailboxMessage> findDeletedMessagesInMailboxAfterUID(long mailboxId, long uid) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .greaterThanOrEqual("id", uid)
            .build(),
        Sort.by("id").descending());
  }

  public List<JamesMailboxMessage> findDeletedMessagesInMailboxWithUID(long mailboxId, long uid) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .equal("id", uid)
            .build(),
        1,
        Sort.by("id").descending());
  }

  public List<JamesMailboxMessage> findDeletedMessagesInMailboxBetweenUIDs(
      long mailboxId, long from, long to) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.builder()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .between("id", from, to)
            .build(),
        Sort.by("id").descending());
  }

  public Optional<JamesMailboxMessage> findMailboxMessageById(MailboxIdUidKey id) {
    return this.mailboxMessageDao.findById(id);
  }

  /**
   * 邮件分页查询
   *
   * @param pageable 分页对象
   * @param filters 筛选条件
   * @return Pager<JamesMailboxMessage>
   */
  public Page<JamesMailboxMessage> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return this.mailboxMessageDao.findWithDetailsPage(pageable, filters);
  }

  public JamesMailboxMessage update(String id, JamesMailboxMessage message, Boolean merge) {
    MailboxIdUidKey key = new MailboxIdUidKey(id);
    message.setMailbox(JamesMailbox.builder().id(key.getMailbox()).build());
    message.setId(key.getId());
    return this.mailboxMessageDao.update(message, merge);
  }

  public void update(JamesMailboxMessage message) {
    this.mailboxMessageDao.update(message);
  }

  public JamesMailboxMessage getMailboxMessageById(long mailboxId, long uid) {
    return this.mailboxMessageDao.getById(new MailboxIdUidKey(mailboxId, uid));
  }
}
