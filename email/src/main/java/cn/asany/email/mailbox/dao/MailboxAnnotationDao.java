package cn.asany.email.mailbox.dao;

import cn.asany.email.mailbox.bean.JPAMailboxAnnotationId;
import cn.asany.email.mailbox.bean.JamesMailboxAnnotation;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailboxAnnotationDao
    extends JpaRepository<JamesMailboxAnnotation, JPAMailboxAnnotationId> {}
