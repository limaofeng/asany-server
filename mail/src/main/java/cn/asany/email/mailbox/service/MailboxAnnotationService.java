package cn.asany.email.mailbox.service;

import cn.asany.email.mailbox.dao.MailboxAnnotationDao;
import cn.asany.email.mailbox.domain.JPAMailboxAnnotationId;
import cn.asany.email.mailbox.domain.JamesMailboxAnnotation;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;

public class MailboxAnnotationService {

  @Autowired private MailboxAnnotationDao mailboxAnnotationDao;

  public List<JamesMailboxAnnotation> retrieveAllAnnotations(long mailboxId) {
    return this.mailboxAnnotationDao.findAll(
        PropertyFilter.builder().equal("mailboxId", mailboxId).build());
  }

  public JamesMailboxAnnotation retrieveByKey(long mailboxId, String key) {
    return this.mailboxAnnotationDao
        .findOne(PropertyFilter.builder().equal("mailboxId", mailboxId).equal("key", key).build())
        .orElse(null);
  }

  public List<JamesMailboxAnnotation> retrieveByKeyLike(long mailboxId, String key) {
    return this.mailboxAnnotationDao.findAll(
        PropertyFilter.builder().equal("mailboxId", mailboxId).contains("key", key).build());
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
        PropertyFilter.builder().equal("mailboxId", mailboxId).build());
  }
}
