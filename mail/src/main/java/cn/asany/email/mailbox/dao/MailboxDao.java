package cn.asany.email.mailbox.dao;

import cn.asany.email.mailbox.domain.JamesMailbox;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Mailbox Dao
 *
 * @author limaofeng
 */
@Repository
public interface MailboxDao extends JpaRepository<JamesMailbox, Long> {}
