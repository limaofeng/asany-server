package cn.asany.email.mailbox.dao;

import cn.asany.email.mailbox.domain.JamesMailboxMessage;
import cn.asany.email.mailbox.domain.toys.MailboxIdUidKey;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Mailbox Message
 *
 * @author limaofeng
 */
@Repository
public interface MailboxMessageDao extends JpaRepository<JamesMailboxMessage, MailboxIdUidKey> {

  @Modifying
  @Query("DELETE FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId")
  int deleteMessages(@Param("mailboxId") Long mailboxId);

  @Modifying
  int deleteDeletedMessagesInMailbox(Long mailbox);

  @Modifying
  @Query(
      "DELETE FROM MailboxMessage message WHERE message.mailbox.id = :mailboxId AND message.id>=:uid AND message.deleted=TRUE")
  int deleteDeletedMessagesInMailboxAfterUID(
      @Param("mailboxId") long mailbox, @Param("uid") long uid);

  int deleteDeletedMessagesInMailboxWithUID(
      @Param("mailboxId") long mailboxId, @Param("uid") long uid);

  int deleteDeletedMessagesInMailboxBetweenUIDs(
      @Param("mailboxId") long mailboxId, @Param("from") long from, @Param("to") long to);

  @EntityGraph(
      value = "Graph.MailboxMessage.FetchDetails",
      type = EntityGraph.EntityGraphType.FETCH)
  Page<JamesMailboxMessage> findWithDetailsPage(Pageable pageable, PropertyFilter filter);

  @Override
  @EntityGraph(
      value = "Graph.MailboxMessage.FetchDetails",
      type = EntityGraph.EntityGraphType.FETCH)
  List<JamesMailboxMessage> findAll(PropertyFilter filter, Sort sort);

  @Override
  @EntityGraph(
      value = "Graph.MailboxMessage.FetchDetails",
      type = EntityGraph.EntityGraphType.FETCH)
  List<JamesMailboxMessage> findAll(PropertyFilter filter, int size, Sort sort);
}
