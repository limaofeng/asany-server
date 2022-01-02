package cn.asany.email.mailbox.service;

import cn.asany.email.mailbox.bean.JamesMailbox;
import cn.asany.email.mailbox.dao.MailboxDao;
import cn.asany.email.mailbox.dao.MailboxMessageDao;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailboxService {

  @Autowired private MailboxDao mailboxDao;
  @Autowired private MailboxMessageDao mailboxMessageDao;

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

  public Optional<JamesMailbox> findMailboxById(long id) {
    return this.mailboxDao.findById(id);
  }

  public void delete(long id) {
    this.mailboxMessageDao.deleteMessages(id);
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
