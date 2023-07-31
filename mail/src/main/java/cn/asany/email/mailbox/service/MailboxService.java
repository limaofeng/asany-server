package cn.asany.email.mailbox.service;

import cn.asany.email.mailbox.dao.MailboxDao;
import cn.asany.email.mailbox.dao.MailboxMessageDao;
import cn.asany.email.mailbox.domain.JamesMailbox;
import cn.asany.email.user.service.MailUserService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮箱服务
 *
 * @author limaofeng
 */
@Slf4j
@Service
public class MailboxService {

  private final MailboxDao mailboxDao;
  private final MailboxMessageDao mailboxMessageDao;

  private static final String CACHE_KEY = "mail::mailbox";

  public MailboxService(MailboxDao mailboxDao, MailboxMessageDao mailboxMessageDao) {
    this.mailboxDao = mailboxDao;
    this.mailboxMessageDao = mailboxMessageDao;
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public JamesMailbox save(JamesMailbox mailbox) {
    return this.mailboxDao.save(mailbox);
  }

  @Transactional(readOnly = true)
  public Optional<JamesMailbox> findMailboxByName(String name, String namespace) {
    return this.mailboxDao.findOne(
        PropertyFilter.newFilter()
            .equal("name", name)
            .isNull("user")
            .equal("namespace", namespace));
  }

  @Transactional(readOnly = true)
  public List<JamesMailbox> findMailboxWithNameLike(String name, String namespace) {
    return this.mailboxDao.findAll(
        PropertyFilter.newFilter()
            .contains("name", name)
            .isNull("user")
            .equal("namespace", namespace));
  }

  @Transactional(readOnly = true)
  public List<JamesMailbox> findMailboxesWithUser(String user) {
    return this.mailboxDao.findAll(PropertyFilter.newFilter().equal("user", user));
  }

  @Transactional(readOnly = true)
  public List<JamesMailbox> findMailboxesWithUser(String user, String namespace) {
    return this.mailboxDao.findAll(
        PropertyFilter.newFilter().equal("user", user).equal("namespace", namespace));
  }

  @Transactional(readOnly = true)
  public List<JamesMailbox> findMailboxWithNameLikeWithUser(
      String name, String user, String namespace) {
    return this.mailboxDao.findAll(
        PropertyFilter.newFilter()
            .contains("name", name)
            .equal("user", user)
            .equal("namespace", namespace));
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "#p1 + '#mailboxByName(' + #p0 + #p2 + ')'", value = MailUserService.CACHE_KEY)
  public Optional<JamesMailbox> findMailboxByNameWithUser(
      String name, String user, String namespace) {
    return this.mailboxDao.findOne(
        PropertyFilter.newFilter()
            .equal("name", name)
            .equal("user", user)
            .equal("namespace", namespace));
  }

  @Transactional(readOnly = true)
  public Optional<JamesMailbox> findMailboxByNameWithUser(String name, String user) {
    return this.mailboxDao.findOne(
        PropertyFilter.newFilter().equal("name", name).equal("user", user));
  }

  @Transactional(readOnly = true)
  public Optional<JamesMailbox> findMailboxById(long id) {
    return this.mailboxDao.findById(id);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public void delete(long id) {
    int rows = this.mailboxMessageDao.deleteMessages(id);
    log.debug("删除{}条消息", rows);
    this.mailboxDao.deleteById(id);
  }

  @Transactional(readOnly = true)
  public long countMailboxesWithNameLike(String name, String namespace) {
    return this.mailboxDao.count(
        PropertyFilter.newFilter()
            .contains("name", name)
            .isNull("user")
            .equal("namespace", namespace));
  }

  @Transactional(readOnly = true)
  public Long countMailboxesWithNameLikeWithUser(String name, String user, String namespace) {
    return this.mailboxDao.count(
        PropertyFilter.newFilter()
            .contains("name", name)
            .equal("user", user)
            .equal("namespace", namespace));
  }

  @Transactional(readOnly = true)
  public List<JamesMailbox> listMailboxes() {
    return this.mailboxDao.findAll();
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "#p0 + '#last'", value = CACHE_KEY)
  public Optional<Long> findLastUid(long id) {
    Optional<JamesMailbox> optional = this.mailboxDao.findById(id);
    return optional.map(JamesMailbox::getLastUid);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public void update(JamesMailbox mailbox) {
    this.mailboxDao.update(mailbox);
  }

  @Transactional(readOnly = true)
  public Optional<Long> findHighestModSeq(long mailbox) {
    Optional<JamesMailbox> optional = this.mailboxDao.findById(mailbox);
    return optional.map(JamesMailbox::getHighestModSeq);
  }
}
