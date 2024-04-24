package cn.asany.email.mailbox.dao;

import cn.asany.email.mailbox.domain.JPAMailboxAnnotationId;
import cn.asany.email.mailbox.domain.JamesMailboxAnnotation;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailboxAnnotationDao
    extends AnyJpaRepository<JamesMailboxAnnotation, JPAMailboxAnnotationId> {}
