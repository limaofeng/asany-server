package cn.asany.email.mailbox.component.mail;

import cn.asany.email.mailbox.component.JPAId;
import cn.asany.email.mailbox.component.JPATransactionalMapper;
import cn.asany.email.mailbox.domain.AbstractJPAMailboxMessage;
import cn.asany.email.mailbox.domain.JamesMailbox;
import cn.asany.email.mailbox.domain.JamesMailboxMessage;
import cn.asany.email.mailbox.domain.toys.MailboxIdUidKey;
import cn.asany.email.mailbox.service.MailboxMessageService;
import cn.asany.email.mailbox.service.MailboxService;
import com.github.steveash.guavate.Guavate;
import com.google.common.collect.Iterators;
import java.util.*;
import javax.mail.Flags;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.MessageUid;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.model.*;
import org.apache.james.mailbox.store.FlagsUpdateCalculator;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mailbox.store.mail.MessageUtils;
import org.apache.james.mailbox.store.mail.ModSeqProvider;
import org.apache.james.mailbox.store.mail.UidProvider;
import org.apache.james.mailbox.store.mail.model.MailboxMessage;
import org.apache.james.mailbox.store.mail.utils.ApplicableFlagCalculator;

public class JPAMessageMapper extends JPATransactionalMapper implements MessageMapper {
  private static final int UNLIMIT_MAX_SIZE = -1;
  private static final int UNLIMITED = -1;

  private final MessageUtils messageMetadataMapper;
  private final MailboxService mailboxService;
  private final MailboxMessageService mailboxMessageService;

  public JPAMessageMapper(
      MailboxSession mailboxSession,
      UidProvider uidProvider,
      ModSeqProvider modSeqProvider,
      EntityManagerFactory entityManagerFactory,
      MailboxService mailboxService,
      MailboxMessageService mailboxMessageService) {
    super(entityManagerFactory);
    this.messageMetadataMapper = new MessageUtils(mailboxSession, uidProvider, modSeqProvider);
    this.mailboxMessageService = mailboxMessageService;
    this.mailboxService = mailboxService;
  }

  @Override
  public MailboxCounters getMailboxCounters(Mailbox mailbox) throws MailboxException {
    return MailboxCounters.builder()
        .count(countMessagesInMailbox(mailbox))
        .unseen(countUnseenMessagesInMailbox(mailbox))
        .build();
  }

  @Override
  public Iterator<MessageUid> listAllMessageUids(final Mailbox mailbox) throws MailboxException {
    return Iterators.transform(
        findInMailbox(mailbox, MessageRange.all(), FetchType.Full, UNLIMITED),
        MailboxMessage::getUid);
  }

  @Override
  public Iterator<MailboxMessage> findInMailbox(
      Mailbox mailbox, MessageRange set, FetchType fType, int max) throws MailboxException {
    try {
      List<MailboxMessage> results;
      MessageUid from = set.getUidFrom();
      final MessageUid to = set.getUidTo();
      final MessageRange.Type type = set.getType();
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();

      switch (type) {
        default:
        case ALL:
          results = findMessagesInMailbox(mailboxId, max);
          break;
        case FROM:
          results = findMessagesInMailboxAfterUID(mailboxId, from, max);
          break;
        case ONE:
          results = findMessagesInMailboxWithUID(mailboxId, from);
          break;
        case RANGE:
          results = findMessagesInMailboxBetweenUIDs(mailboxId, from, to, max);
          break;
      }

      return results.iterator();

    } catch (PersistenceException e) {
      throw new MailboxException(
          "Search of MessageRange " + set + " failed in mailbox " + mailbox, e);
    }
  }

  @Override
  public long countMessagesInMailbox(Mailbox mailbox) throws MailboxException {
    try {
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();
      return this.mailboxMessageService.countMessagesInMailbox(mailboxId.getRawId());
    } catch (PersistenceException e) {
      throw new MailboxException("Count of messages failed in mailbox " + mailbox, e);
    }
  }

  @Override
  public long countUnseenMessagesInMailbox(Mailbox mailbox) throws MailboxException {
    try {
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();
      return this.mailboxMessageService.countUnseenMessagesInMailbox(mailboxId.getRawId());
    } catch (PersistenceException e) {
      throw new MailboxException("Count of unseen messages failed in mailbox " + mailbox, e);
    }
  }

  @Override
  public void delete(Mailbox mailbox, MailboxMessage message) throws MailboxException {
    try {
      this.mailboxMessageService.deleteMessages(buildKey(mailbox, message));
    } catch (PersistenceException e) {
      throw new MailboxException(
          "Delete of message " + message + " failed in mailbox " + mailbox, e);
    }
  }

  private MailboxIdUidKey buildKey(Mailbox mailbox, MailboxMessage message) {
    JPAId mailboxId = (JPAId) mailbox.getMailboxId();
    MailboxIdUidKey key = new MailboxIdUidKey();
    key.setMailbox(mailboxId.getRawId());
    key.setId(message.getUid().asLong());
    return key;
  }

  @Override
  public MessageUid findFirstUnseenMessageUid(Mailbox mailbox) throws MailboxException {
    try {
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();
      List<JamesMailboxMessage> result =
          this.mailboxMessageService.findUnseenMessagesInMailboxOrderByUid(mailboxId.getRawId(), 1);
      if (result.isEmpty()) {
        return null;
      } else {
        return result.get(0).getUid();
      }
    } catch (PersistenceException e) {
      throw new MailboxException("Search of first unseen message failed in mailbox " + mailbox, e);
    }
  }

  @Override
  public List<MessageUid> findRecentMessageUidsInMailbox(Mailbox mailbox) throws MailboxException {
    try {
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();
      return this.mailboxMessageService.findRecentMessageUidsInMailbox(mailboxId.getRawId());
    } catch (PersistenceException e) {
      throw new MailboxException("Search of recent messages failed in mailbox " + mailbox, e);
    }
  }

  @Override
  public List<MessageUid> retrieveMessagesMarkedForDeletion(
      Mailbox mailbox, MessageRange messageRange) throws MailboxException {
    try {
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();
      List<MailboxMessage> messages = findDeletedMessages(messageRange, mailboxId);
      return getUidList(messages);
    } catch (PersistenceException e) {
      throw new MailboxException(
          "Search of MessageRange " + messageRange + " failed in mailbox " + mailbox, e);
    }
  }

  @Override
  public Map<MessageUid, MessageMetaData> deleteMessages(Mailbox mailbox, List<MessageUid> uids)
      throws MailboxException {
    JPAId mailboxId = (JPAId) mailbox.getMailboxId();
    Map<MessageUid, MessageMetaData> data = new HashMap<>();
    List<MessageRange> ranges = MessageRange.toRanges(uids);

    ranges.forEach(
        range -> {
          List<MailboxMessage> messages = findDeletedMessages(range, mailboxId);
          data.putAll(createMetaData(messages));
          deleteDeletedMessages(range, mailboxId);
        });

    return data;
  }

  private List<MailboxMessage> findDeletedMessages(MessageRange messageRange, JPAId mailboxId) {
    MessageUid from = messageRange.getUidFrom();
    MessageUid to = messageRange.getUidTo();

    switch (messageRange.getType()) {
      case ONE:
        return findDeletedMessagesInMailboxWithUID(mailboxId, from);
      case RANGE:
        return findDeletedMessagesInMailboxBetweenUIDs(mailboxId, from, to);
      case FROM:
        return findDeletedMessagesInMailboxAfterUID(mailboxId, from);
      case ALL:
        return findDeletedMessagesInMailbox(mailboxId);
      default:
        throw new RuntimeException(
            "Cannot find deleted messages, range type "
                + messageRange.getType()
                + " doesn't exist");
    }
  }

  private void deleteDeletedMessages(MessageRange messageRange, JPAId mailboxId) {
    MessageUid from = messageRange.getUidFrom();
    MessageUid to = messageRange.getUidTo();

    switch (messageRange.getType()) {
      case ONE:
        deleteDeletedMessagesInMailboxWithUID(mailboxId, from);
        break;
      case RANGE:
        deleteDeletedMessagesInMailboxBetweenUIDs(mailboxId, from, to);
        break;
      case FROM:
        deleteDeletedMessagesInMailboxAfterUID(mailboxId, from);
        break;
      case ALL:
        deleteDeletedMessagesInMailbox(mailboxId);
        break;
      default:
        throw new RuntimeException(
            "Cannot delete messages, range type " + messageRange.getType() + " doesn't exist");
    }
  }

  @Override
  public MessageMetaData move(Mailbox mailbox, MailboxMessage original) throws MailboxException {
    JPAId originalMailboxId = (JPAId) original.getMailboxId();

    Optional<JamesMailbox> optional = mailboxService.findMailboxById(originalMailboxId.getRawId());

    JamesMailbox originalMailbox = optional.orElse(null);

    MessageMetaData messageMetaData = copy(mailbox, original);

    assert originalMailbox != null;
    delete(originalMailbox.toMailbox(), original);

    return messageMetaData;
  }

  @Override
  public MessageMetaData add(Mailbox mailbox, MailboxMessage message) throws MailboxException {
    messageMetadataMapper.enrichMessage(mailbox, message);

    return save(mailbox, message);
  }

  @Override
  public Iterator<UpdatedFlags> updateFlags(
      Mailbox mailbox, FlagsUpdateCalculator flagsUpdateCalculator, MessageRange set)
      throws MailboxException {
    Iterator<MailboxMessage> messages =
        findInMailbox(mailbox, set, FetchType.Metadata, UNLIMIT_MAX_SIZE);

    MessageUtils.MessageChangedFlags messageChangedFlags =
        messageMetadataMapper.updateFlags(mailbox, flagsUpdateCalculator, messages);

    for (MailboxMessage mailboxMessage : messageChangedFlags.getChangedFlags()) {
      save(mailbox, mailboxMessage);
    }

    return messageChangedFlags.getUpdatedFlags();
  }

  @Override
  public MessageMetaData copy(Mailbox mailbox, MailboxMessage original) throws MailboxException {
    return copy(
        mailbox,
        messageMetadataMapper.nextUid(mailbox),
        messageMetadataMapper.nextModSeq(mailbox),
        original);
  }

  @Override
  public Optional<MessageUid> getLastUid(Mailbox mailbox) throws MailboxException {
    return messageMetadataMapper.getLastUid(mailbox);
  }

  @Override
  public long getHighestModSeq(Mailbox mailbox) throws MailboxException {
    return messageMetadataMapper.getHighestModSeq(mailbox);
  }

  @Override
  public Flags getApplicableFlag(Mailbox mailbox) throws MailboxException {
    int maxBatchSize = -1;
    return new ApplicableFlagCalculator(
            findMessagesInMailbox((JPAId) mailbox.getMailboxId(), maxBatchSize))
        .computeApplicableFlags();
  }

  private MessageMetaData copy(
      Mailbox mailbox, MessageUid uid, long modSeq, MailboxMessage original)
      throws MailboxException {
    JamesMailbox currentMailbox = JamesMailbox.from(mailbox);
    MailboxMessage copy = new JamesMailboxMessage(currentMailbox, uid, modSeq, original);

    return save(mailbox, copy);
  }

  protected MessageMetaData save(Mailbox mailbox, MailboxMessage message) throws MailboxException {
    try {
      JPAId mailboxId = (JPAId) mailbox.getMailboxId();
      Optional<JamesMailbox> optional = this.mailboxService.findMailboxById(mailboxId.getRawId());
      if (!optional.isPresent()) {
        throw new MailboxException("不存在文件夹");
      }
      JamesMailbox currentMailbox = optional.get();
      if (message instanceof AbstractJPAMailboxMessage) {
        ((AbstractJPAMailboxMessage) message).setMailbox(currentMailbox);
        this.mailboxMessageService.save((JamesMailboxMessage) message);
        return message.metaData();
      } else {
        JamesMailboxMessage persistData =
            new JamesMailboxMessage(currentMailbox, message.getUid(), message.getModSeq(), message);
        persistData.setFlags(message.createFlags());

        this.mailboxMessageService.save(persistData);
        return persistData.metaData();
      }

    } catch (PersistenceException e) {
      throw new MailboxException("Save of message " + message + " failed in mailbox " + mailbox, e);
    }
  }

  private List<MailboxMessage> findMessagesInMailboxAfterUID(
      JPAId mailboxId, MessageUid from, int batchSize) {
    return new ArrayList<>(
        this.mailboxMessageService.findMessagesInMailboxAfterUID(
            mailboxId.getRawId(), from.asLong(), batchSize));
  }

  private List<MailboxMessage> findMessagesInMailboxWithUID(JPAId mailboxId, MessageUid from) {
    return new ArrayList<>(
        this.mailboxMessageService.findMessagesInMailboxWithUID(
            mailboxId.getRawId(), from.asLong()));
  }

  private List<MailboxMessage> findMessagesInMailboxBetweenUIDs(
      JPAId mailboxId, MessageUid from, MessageUid to, int batchSize) {
    return new ArrayList<>(
        this.mailboxMessageService.findMessagesInMailboxBetweenUIDs(
            mailboxId.getRawId(), from.asLong(), to.asLong(), batchSize));
  }

  private List<MailboxMessage> findMessagesInMailbox(JPAId mailboxId, int batchSize) {
    return new ArrayList<>(
        this.mailboxMessageService.findMessagesInMailbox(mailboxId.getRawId(), batchSize));
  }

  private Map<MessageUid, MessageMetaData> createMetaData(List<MailboxMessage> uids) {
    final Map<MessageUid, MessageMetaData> data = new HashMap<>();
    for (MailboxMessage m : uids) {
      data.put(m.getUid(), m.metaData());
    }
    return data;
  }

  private List<MessageUid> getUidList(List<MailboxMessage> messages) {
    return messages.stream().map(MailboxMessage::getUid).collect(Guavate.toImmutableList());
  }

  private int deleteDeletedMessagesInMailbox(JPAId mailboxId) {
    return this.mailboxMessageService.deleteDeletedMessagesInMailbox(mailboxId.getRawId());
  }

  private int deleteDeletedMessagesInMailboxAfterUID(JPAId mailboxId, MessageUid from) {
    return this.mailboxMessageService.deleteDeletedMessagesInMailboxAfterUID(
        mailboxId.getRawId(), from.asLong());
  }

  private int deleteDeletedMessagesInMailboxWithUID(JPAId mailboxId, MessageUid from) {
    return this.mailboxMessageService.deleteDeletedMessagesInMailboxWithUID(
        mailboxId.getRawId(), from.asLong());
  }

  private int deleteDeletedMessagesInMailboxBetweenUIDs(
      JPAId mailboxId, MessageUid from, MessageUid to) {
    return this.mailboxMessageService.deleteDeletedMessagesInMailboxBetweenUIDs(
        mailboxId.getRawId(), from.asLong(), to.asLong());
  }

  private List<MailboxMessage> findDeletedMessagesInMailbox(JPAId mailboxId) {
    return new ArrayList<>(
        this.mailboxMessageService.findDeletedMessagesInMailbox(mailboxId.getRawId()));
  }

  private List<MailboxMessage> findDeletedMessagesInMailboxAfterUID(
      JPAId mailboxId, MessageUid from) {
    return new ArrayList<>(
        this.mailboxMessageService.findDeletedMessagesInMailboxAfterUID(
            mailboxId.getRawId(), from.asLong()));
  }

  private List<MailboxMessage> findDeletedMessagesInMailboxWithUID(
      JPAId mailboxId, MessageUid from) {
    return new ArrayList<>(
        this.mailboxMessageService.findDeletedMessagesInMailboxWithUID(
            mailboxId.getRawId(), from.asLong()));
  }

  private List<MailboxMessage> findDeletedMessagesInMailboxBetweenUIDs(
      JPAId mailboxId, MessageUid from, MessageUid to) {
    return new ArrayList<>(
        this.mailboxMessageService.findDeletedMessagesInMailboxBetweenUIDs(
            mailboxId.getRawId(), from.asLong(), to.asLong()));
  }
}
