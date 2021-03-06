package cn.asany.email.mailbox.service;

import cn.asany.email.mailbox.dao.MailboxDao;
import cn.asany.email.mailbox.dao.MailboxMessageDao;
import cn.asany.email.mailbox.domain.JamesMailbox;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮箱服务
 *
 * @author limaofeng
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MailboxService {

  private final MailboxDao mailboxDao;
  private final MailboxMessageDao mailboxMessageDao;

  public MailboxService(MailboxDao mailboxDao, MailboxMessageDao mailboxMessageDao) {
    this.mailboxDao = mailboxDao;
    this.mailboxMessageDao = mailboxMessageDao;
  }

  public JamesMailbox save(JamesMailbox mailbox) {
    return this.mailboxDao.save(mailbox);
  }

  public Optional<JamesMailbox> findMailboxByName(String name, String namespace) {
    return this.mailboxDao.findOne(
        PropertyFilter.builder()
            .equal("name", name)
            .isNull("user")
            .equal("namespace", namespace)
            .build());
  }

  public List<JamesMailbox> findMailboxWithNameLike(String name, String namespace) {
    return this.mailboxDao.findAll(
        PropertyFilter.builder()
            .contains("name", name)
            .isNull("user")
            .equal("namespace", namespace)
            .build());
  }

  public List<JamesMailbox> findMailboxesWithUser(String user) {
    return this.mailboxDao.findAll(PropertyFilter.builder().equal("user", user).build());
  }

  public List<JamesMailbox> findMailboxesWithUser(String user, String namespace) {
    return this.mailboxDao.findAll(
        PropertyFilter.builder().equal("user", user).equal("namespace", namespace).build());
  }

  public List<JamesMailbox> findMailboxWithNameLikeWithUser(
      String name, String user, String namespace) {
    return this.mailboxDao.findAll(
        PropertyFilter.builder()
            .contains("name", name)
            .equal("user", user)
            .equal("namespace", namespace)
            .build());
  }

  public Optional<JamesMailbox> findMailboxByNameWithUser(
      String name, String user, String namespace) {
    return this.mailboxDao.findOne(
        PropertyFilter.builder()
            .equal("name", name)
            .equal("user", user)
            .equal("namespace", namespace)
            .build());
  }

  public Optional<JamesMailbox> findMailboxByNameWithUser(String name, String user) {
    return this.mailboxDao.findOne(
        PropertyFilter.builder().equal("name", name).equal("user", user).build());
  }

  public Optional<JamesMailbox> findMailboxById(long id) {
    return this.mailboxDao.findById(id);
  }

  public void delete(long id) {
    int rows = this.mailboxMessageDao.deleteMessages(id);
    this.mailboxDao.deleteById(id);
  }

  public long countMailboxesWithNameLike(String name, String namespace) {
    return this.mailboxDao.count(
        PropertyFilter.builder()
            .contains("name", name)
            .isNull("user")
            .equal("namespace", namespace)
            .build());
  }

  public Long countMailboxesWithNameLikeWithUser(String name, String user, String namespace) {
    return this.mailboxDao.count(
        PropertyFilter.builder()
            .contains("name", name)
            .equal("user", user)
            .equal("namespace", namespace)
            .build());
  }

  public List<JamesMailbox> listMailboxes() {
    return this.mailboxDao.findAll();
  }

  public Optional<Long> findLastUid(long id) {
    Optional<JamesMailbox> optional = this.mailboxDao.findById(id);
    return optional.map(JamesMailbox::getLastUid);
  }

  public void update(JamesMailbox mailbox) {
    this.mailboxDao.update(mailbox);
  }

  public Optional<Long> findHighestModSeq(long mailbox) {
    Optional<JamesMailbox> optional = this.mailboxDao.findById(mailbox);
    return optional.map(JamesMailbox::getHighestModSeq);
  }
}
