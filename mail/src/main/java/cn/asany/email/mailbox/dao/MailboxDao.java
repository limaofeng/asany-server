package cn.asany.email.mailbox.dao;

import cn.asany.email.mailbox.domain.JamesMailbox;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Mailbox Dao
 *
 * @author limaofeng
 */
@Repository
public interface MailboxDao extends AnyJpaRepository<JamesMailbox, Long> {}
