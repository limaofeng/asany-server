package cn.asany.email.mailbox.service;

import cn.asany.email.mailbox.dao.MailboxMessageDao;
import cn.asany.email.mailbox.domain.*;
import cn.asany.email.mailbox.domain.toys.MailboxIdUidKey;
import cn.asany.email.utils.SentMailContext;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.james.mailbox.DefaultMailboxes;
import org.apache.james.mailbox.MessageUid;
import org.apache.james.mailbox.store.mail.model.Property;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailboxMessageService {

  private static final String CACHE_KEY = "mail::mailbox";

  private final MailboxMessageDao mailboxMessageDao;

  public MailboxMessageService(MailboxMessageDao mailboxMessageDao) {
    this.mailboxMessageDao = mailboxMessageDao;
  }

  public void deleteMessages(MailboxIdUidKey... ids) {
    this.mailboxMessageDao.deleteAllById(Arrays.asList(ids));
  }

  /**
   * 文件夹下的邮件数量
   *
   * @param mailbox 文件夹ID
   * @return long 数量
   */
  @Transactional(readOnly = true)
  @Cacheable(key = "#p0 + '#count'", value = CACHE_KEY)
  public long countMessagesInMailbox(long mailbox) {
    return this.mailboxMessageDao.count(PropertyFilter.newFilter().equal("mailbox.id", mailbox));
  }

  /**
   * 文件夹下的未读邮件数量
   *
   * @param mailbox 文件夹ID
   * @return long 数量
   */
  @Transactional(readOnly = true)
  @Cacheable(key = "#p0 + '#unseenCount'", value = CACHE_KEY)
  public long countUnseenMessagesInMailbox(long mailbox) {
    return this.mailboxMessageDao.count(
        PropertyFilter.newFilter().equal("mailbox.id", mailbox).equal("seen", Boolean.FALSE));
  }

  /**
   * 文件夹下的未读邮件
   *
   * @param mailbox 文件夹ID
   * @param size 数量，如果为 -1 返回全部
   * @return List<JamesMailboxMessage>
   */
  @Transactional(readOnly = true)
  @Cacheable(key = "#p0 + '#unseen(size=' + #p1 + ')'", value = CACHE_KEY)
  public List<JamesMailboxMessage> findUnseenMessagesInMailboxOrderByUid(long mailbox, int size) {
    return this.mailboxMessageDao
        .findPage(
            PageRequest.of(0, size, Sort.by("id").ascending()),
            PropertyFilter.newFilter().equal("mailbox.id", mailbox).equal("seen", Boolean.FALSE))
        .getContent();
  }

  /**
   * 查找最近的邮件
   *
   * @param mailbox 邮箱ID
   * @return List<MessageUid>
   */
  @Cacheable(key = "#p0 + '#recent'", value = CACHE_KEY)
  public List<MessageUid> findRecentMessageUidsInMailbox(long mailbox) {
    return this.mailboxMessageDao
        .findAll(
            PropertyFilter.newFilter().equal("mailbox.id", mailbox).equal("recent", Boolean.TRUE),
            Sort.by("id").descending())
        .stream()
        .map(AbstractJPAMailboxMessage::getUid)
        .collect(Collectors.toList());
  }

  /**
   * 保存邮件
   *
   * @param message 邮件
   * @return JamesMailboxMessage
   */
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

  /**
   * 查找指定ID之后的邮件
   *
   * @param mailbox 邮箱ID
   * @param uid 邮件ID
   * @param size 数量
   * @return List<JamesMailboxMessage>
   */
  @Cacheable(
      key = "#p0 + '#messagesInMailboxAfterUID(uid='+#p1+',size=' + #p2+ ')'",
      value = CACHE_KEY)
  public List<JamesMailboxMessage> findMessagesInMailboxAfterUID(long mailbox, long uid, int size) {
    PropertyFilter filter =
        PropertyFilter.newFilter().equal("mailbox.id", mailbox).greaterThanOrEqual("id", uid);
    return this.mailboxMessageDao.findAll(filter, size, Sort.by("id").descending());
  }

  /**
   * 获取文件夹下的邮件
   *
   * @param mailbox 文件夹ID
   * @param uid 邮件ID
   * @return JamesMailboxMessage
   */
  @Cacheable(key = "#p0 + '#messagesInMailboxWithUID(uid='+#p1+')'", value = CACHE_KEY)
  public List<JamesMailboxMessage> findMessagesInMailboxWithUID(long mailbox, long uid) {
    PropertyFilter filter =
        PropertyFilter.newFilter().equal("mailbox.id", mailbox).equal("id", uid);
    return this.mailboxMessageDao.findAll(filter, Sort.by("id").descending());
  }

  @Cacheable(key = "#p0 + '#messagesInMailbox(size='+#p1+')'", value = CACHE_KEY)
  public List<JamesMailboxMessage> findMessagesInMailbox(long mailbox, int size) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.newFilter().equal("mailbox.id", mailbox), size, Sort.by("id").descending());
  }

  /**
   * 获取文件夹下ID 区间的邮件
   *
   * @param mailbox 文件夹ID
   * @param from 开始ID
   * @param to 结束ID
   * @param size 数量
   * @return List<JamesMailboxMessage> 邮件列表
   */
  @Cacheable(
      key = "#p0 + '#messagesInMailboxBetweenUIDs(from='+#p1+'to='+#p2+')'",
      value = CACHE_KEY)
  public List<JamesMailboxMessage> findMessagesInMailboxBetweenUIDs(
      long mailbox, long from, long to, int size) {
    PropertyFilter filter =
        PropertyFilter.newFilter().equal("mailbox.id", mailbox).between("id", from, to);
    return this.mailboxMessageDao.findAll(filter, size, Sort.by("id").descending());
  }

  public long index(Date internalDate, long mailbox) {
    return this.mailboxMessageDao.count(
        PropertyFilter.newFilter()
            .greaterThanOrEqual("internalDate", internalDate)
            .equal("mailbox.id", mailbox));
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public int deleteDeletedMessagesInMailbox(long mailbox) {
    return this.mailboxMessageDao.deleteDeletedMessagesInMailbox(mailbox);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public int deleteDeletedMessagesInMailboxAfterUID(long mailbox, long uid) {
    return this.mailboxMessageDao.deleteDeletedMessagesInMailboxAfterUID(mailbox, uid);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public int deleteDeletedMessagesInMailboxWithUID(long mailboxId, long uid) {
    return this.mailboxMessageDao.deleteDeletedMessagesInMailboxWithUID(mailboxId, uid);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public int deleteDeletedMessagesInMailboxBetweenUIDs(long mailboxId, long from, long to) {
    return this.mailboxMessageDao.deleteDeletedMessagesInMailboxBetweenUIDs(mailboxId, from, to);
  }

  @Cacheable(key = "#p0 + '#deletedMessagesInMailbox'", value = CACHE_KEY)
  public List<JamesMailboxMessage> findDeletedMessagesInMailbox(long mailboxId) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.newFilter().equal("mailbox.id", mailboxId).equal("deleted", Boolean.TRUE),
        Sort.by("id").descending());
  }

  @Cacheable(key = "#p0 + '#deletedMessagesInMailboxAfterUID(id='+#p1+')'", value = CACHE_KEY)
  public List<JamesMailboxMessage> findDeletedMessagesInMailboxAfterUID(long mailboxId, long uid) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.newFilter()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .greaterThanOrEqual("id", uid),
        Sort.by("id").descending());
  }

  @Cacheable(key = "#p0 + '#deletedMessagesInMailboxWithUID(id='+#p1+')'", value = CACHE_KEY)
  public List<JamesMailboxMessage> findDeletedMessagesInMailboxWithUID(long mailboxId, long uid) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.newFilter()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .equal("id", uid),
        1,
        Sort.by("id").descending());
  }

  @Cacheable(
      key = "#p0 + '#deletedMessagesInMailboxBetweenUIDs(from='+#p1+'to='+#p2+')'",
      value = CACHE_KEY)
  public List<JamesMailboxMessage> findDeletedMessagesInMailboxBetweenUIDs(
      long mailboxId, long from, long to) {
    return this.mailboxMessageDao.findAll(
        PropertyFilter.newFilter()
            .equal("mailbox.id", mailboxId)
            .equal("deleted", Boolean.TRUE)
            .between("id", from, to),
        Sort.by("id").descending());
  }

  @Cacheable(key = "#p0.toString() + '#mailboxMessageById'", value = CACHE_KEY)
  public Optional<JamesMailboxMessage> findMailboxMessageById(MailboxIdUidKey id) {
    return this.mailboxMessageDao.findById(id);
  }

  /**
   * 邮件分页查询
   *
   * @param pageable 分页对象
   * @param filter 筛选条件
   * @return Pager<JamesMailboxMessage>
   */
  public Page<JamesMailboxMessage> findPage(Pageable pageable, PropertyFilter filter) {
    return this.mailboxMessageDao.findWithDetailsPage(pageable, filter);
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
    return this.mailboxMessageDao.getReferenceById(new MailboxIdUidKey(mailboxId, uid));
  }
}
