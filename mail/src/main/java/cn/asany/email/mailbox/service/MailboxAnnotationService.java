package cn.asany.email.mailbox.service;

import cn.asany.email.mailbox.dao.MailboxAnnotationDao;
import cn.asany.email.mailbox.domain.JPAMailboxAnnotationId;
import cn.asany.email.mailbox.domain.JamesMailboxAnnotation;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

@Service
public class MailboxAnnotationService {

  private final MailboxAnnotationDao mailboxAnnotationDao;

  public MailboxAnnotationService(MailboxAnnotationDao mailboxAnnotationDao) {
    this.mailboxAnnotationDao = mailboxAnnotationDao;
  }

  public List<JamesMailboxAnnotation> retrieveAllAnnotations(long mailboxId) {
    return this.mailboxAnnotationDao.findAll(
        PropertyFilter.newFilter().equal("mailboxId", mailboxId));
  }

  public JamesMailboxAnnotation retrieveByKey(long mailboxId, String key) {
    return this.mailboxAnnotationDao
        .findOne(PropertyFilter.newFilter().equal("mailboxId", mailboxId).equal("key", key))
        .orElse(null);
  }

  public List<JamesMailboxAnnotation> retrieveByKeyLike(long mailboxId, String key) {
    return this.mailboxAnnotationDao.findAll(
        PropertyFilter.newFilter().equal("mailboxId", mailboxId).contains("key", key));
  }

  public Optional<JamesMailboxAnnotation> findById(JPAMailboxAnnotationId id) {
    return this.mailboxAnnotationDao.findById(id);
  }

  public void delete(JPAMailboxAnnotationId id) {
    this.mailboxAnnotationDao.deleteById(id);
  }

  public void save(JamesMailboxAnnotation mailboxAnnotation) {
    this.mailboxAnnotationDao.save(mailboxAnnotation);
  }

  public void update(JamesMailboxAnnotation entity) {
    this.mailboxAnnotationDao.update(entity);
  }

  public JamesMailboxAnnotation getById(JPAMailboxAnnotationId id) {
    return this.mailboxAnnotationDao.getReferenceById(id);
  }

  public long countAnnotationsInMailbox(long mailboxId) {
    return this.mailboxAnnotationDao.count(
        PropertyFilter.newFilter().equal("mailboxId", mailboxId));
  }
}
