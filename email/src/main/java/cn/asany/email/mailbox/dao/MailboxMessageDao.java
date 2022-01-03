package cn.asany.email.mailbox.dao;

import cn.asany.email.mailbox.bean.AbstractJPAMailboxMessage.MailboxIdUidKey;
import cn.asany.email.mailbox.bean.JamesMailboxMessage;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MailboxMessageDao extends JpaRepository<JamesMailboxMessage, MailboxIdUidKey> {

  @Query("DELETE FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId")
  int deleteMessages(@Param("mailboxId") Long mailboxId);

  @Query(
      "DELETE FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId AND message.deleted=TRUE")
  int deleteDeletedMessagesInMailbox(@Param("mailboxId") Long mailbox);

  @Query(
      "DELETE FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId AND message.id>=:uid AND message.deleted=TRUE")
  int deleteDeletedMessagesInMailboxAfterUID(
      @Param("mailboxId") long mailbox, @Param("uid") long uid);

  @Query(
      "DELETE FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId AND message.id=:uid AND message.deleted=TRUE")
  int deleteDeletedMessagesInMailboxWithUID(
      @Param("mailboxId") long mailboxId, @Param("uid") long uid);

  @Query(
      "DELETE FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId AND message.id BETWEEN :from AND :to AND message.deleted=TRUE")
  int deleteDeletedMessagesInMailboxBetweenUIDs(
      @Param("mailboxId") long mailboxId, @Param("from") long from, @Param("to") long to);
}
