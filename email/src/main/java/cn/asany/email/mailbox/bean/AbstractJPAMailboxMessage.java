package cn.asany.email.mailbox.bean;

import cn.asany.email.mailbox.component.JPAId;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.util.*;
import javax.mail.Flags;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.james.mailbox.MessageUid;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.model.ComposedMessageId;
import org.apache.james.mailbox.model.ComposedMessageIdWithMetaData;
import org.apache.james.mailbox.model.MessageAttachment;
import org.apache.james.mailbox.model.MessageId;
import org.apache.james.mailbox.store.mail.model.DefaultMessageId;
import org.apache.james.mailbox.store.mail.model.FlagsFactory;
import org.apache.james.mailbox.store.mail.model.MailboxMessage;
import org.apache.james.mailbox.store.mail.model.Property;
import org.apache.james.mailbox.store.mail.model.impl.MessageParser;
import org.apache.james.mailbox.store.mail.model.impl.PropertyBuilder;
import org.apache.james.mailbox.store.search.comparator.UidComparator;
import org.apache.james.mime4j.MimeException;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumns;
import org.apache.openjpa.persistence.jdbc.Index;

@Getter
@Setter
@IdClass(AbstractJPAMailboxMessage.MailboxIdUidKey.class)
@MappedSuperclass
public abstract class AbstractJPAMailboxMessage implements MailboxMessage {

  private static final Comparator<MailboxMessage> MESSAGE_UID_COMPARATOR = new UidComparator();
  private static final String TOSTRING_SEPARATOR = " ";

  @Id
  @ManyToOne(
      cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinColumn(name = "MAILBOX_ID", foreignKey = @ForeignKey(name = "FK_JAMES_MAIL_MAILBOX"))
  private JamesMailbox mailbox;

  /** The value for the uid field */
  @Id
  @Column(name = "MAIL_UID")
  private long uid;

  /** The value for the modSeq field */
  @org.apache.openjpa.persistence.jdbc.Index
  @Column(name = "MAIL_MODSEQ")
  private long modSeq;

  /** The value for the internalDate field */
  @Basic(optional = false)
  @Column(name = "MAIL_DATE")
  private Date internalDate;

  /** The value for the answered field */
  @Basic(optional = false)
  @Column(name = "MAIL_IS_ANSWERED", nullable = false)
  private boolean answered = false;

  /** The value for the deleted field */
  @Basic(optional = false)
  @Column(name = "MAIL_IS_DELETED", nullable = false)
  @org.apache.openjpa.persistence.jdbc.Index
  private boolean deleted = false;

  /** The value for the draft field */
  @Basic(optional = false)
  @Column(name = "MAIL_IS_DRAFT", nullable = false)
  private boolean draft = false;

  /** The value for the flagged field */
  @Basic(optional = false)
  @Column(name = "MAIL_IS_FLAGGED", nullable = false)
  private boolean flagged = false;

  /** The value for the recent field */
  @Basic(optional = false)
  @Column(name = "MAIL_IS_RECENT", nullable = false)
  @org.apache.openjpa.persistence.jdbc.Index
  private boolean recent = false;

  /** The value for the seen field */
  @Basic(optional = false)
  @Column(name = "MAIL_IS_SEEN", nullable = false)
  @Index
  private boolean seen = false;

  /** The first body octet */
  @Basic(optional = false)
  @Column(name = "MAIL_BODY_START_OCTET", nullable = false)
  private int bodyStartOctet;

  /** Number of octets in the full document content */
  @Basic(optional = false)
  @Column(name = "MAIL_CONTENT_OCTETS_COUNT", nullable = false)
  private long contentOctets;

  /** MIME media type */
  @Basic(optional = true)
  @Column(name = "MAIL_MIME_TYPE", nullable = true, length = 200)
  private String mediaType;

  /** MIME sub type */
  @Basic(optional = true)
  @Column(name = "MAIL_MIME_SUBTYPE", nullable = true, length = 200)
  private String subType;

  /** THE CRFL count when this document is textual, null otherwise */
  @Basic(optional = true)
  @Column(name = "MAIL_TEXTUAL_LINE_COUNT", nullable = true)
  private Long textualLineCount;

  /** Meta data for this message */
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @OrderBy("line")
  @ElementJoinColumns({
    @ElementJoinColumn(name = "MAILBOX_ID", referencedColumnName = "MAILBOX_ID"),
    @ElementJoinColumn(name = "MAIL_UID", referencedColumnName = "MAIL_UID")
  })
  private Set<JamesProperty> properties;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("id")
  @ElementJoinColumns({
    @ElementJoinColumn(name = "MAILBOX_ID", referencedColumnName = "MAILBOX_ID"),
    @ElementJoinColumn(name = "MAIL_UID", referencedColumnName = "MAIL_UID")
  })
  private Set<JamesUserFlag> userFlags;

  public AbstractJPAMailboxMessage() {}

  public AbstractJPAMailboxMessage(
      JamesMailbox mailbox, MessageUid uid, long modSeq, MailboxMessage original)
      throws MailboxException {
    super();
    this.mailbox = mailbox;
    this.uid = uid.asLong();
    this.modSeq = modSeq;
    this.userFlags = new HashSet<>();
    setFlags(original.createFlags());

    // A copy of a message is recent
    // See MAILBOX-85
    this.recent = true;

    this.contentOctets = original.getFullContentOctets();
    this.bodyStartOctet = (int) (original.getFullContentOctets() - original.getBodyOctets());
    this.internalDate = original.getInternalDate();

    PropertyBuilder pBuilder = new PropertyBuilder(original.getProperties());
    this.textualLineCount = original.getTextualLineCount();
    this.mediaType = original.getMediaType();
    this.subType = original.getSubType();
    final List<Property> properties = pBuilder.toProperties();
    this.properties = new HashSet<>(properties.size());
    int order = 0;
    for (Property property : properties) {
      this.properties.add(new JamesProperty(property, order++));
    }
  }

  public AbstractJPAMailboxMessage(
      JamesMailbox mailbox,
      Date internalDate,
      Flags flags,
      long contentOctets,
      int bodyStartOctet,
      PropertyBuilder propertyBuilder) {
    this.mailbox = mailbox;
    this.internalDate = internalDate;
    userFlags = new HashSet<>();

    setFlags(flags);
    this.contentOctets = contentOctets;
    this.bodyStartOctet = bodyStartOctet;
    this.textualLineCount = propertyBuilder.getTextualLineCount();
    this.mediaType = propertyBuilder.getMediaType();
    this.subType = propertyBuilder.getSubType();
    final List<Property> properties = propertyBuilder.toProperties();
    this.properties = new HashSet<>(properties.size());
    int order = 0;
    for (Property property : properties) {
      this.properties.add(new JamesProperty(property, order++));
    }
  }

  @Embeddable
  public static class MailboxIdUidKey implements Serializable {

    private static final long serialVersionUID = 7847632032426660997L;

    public MailboxIdUidKey() {}

    /** The value for the mailbox field */
    public long mailbox;

    /** The value for the uid field */
    public long uid;

    @Override
    public int hashCode() {
      final int PRIME = 31;
      int result = 1;
      result = PRIME * result + (int) (mailbox ^ (mailbox >>> 32));
      result = PRIME * result + (int) (uid ^ (uid >>> 32));
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final MailboxIdUidKey other = (MailboxIdUidKey) obj;
      if (mailbox != other.mailbox) {
        return false;
      }
      return uid == other.uid;
    }
  }

  @Override
  public ComposedMessageIdWithMetaData getComposedMessageIdWithMetaData() {
    return ComposedMessageIdWithMetaData.builder()
        .modSeq(modSeq)
        .flags(createFlags())
        .composedMessageId(
            new ComposedMessageId(mailbox.getMailboxId(), getMessageId(), MessageUid.of(uid)))
        .build();
  }

  @Override
  public long getModSeq() {
    return modSeq;
  }

  @Override
  public void setModSeq(long modSeq) {
    this.modSeq = modSeq;
  }

  @Override
  public String getMediaType() {
    return mediaType;
  }

  @Override
  public String getSubType() {
    return subType;
  }

  /**
   * Gets a read-only list of meta-data properties. For properties with multiple values, this list
   * will contain several enteries with the same namespace and local name.
   *
   * @return unmodifiable list of meta-data, not null
   */
  @Override
  public List<Property> getProperties() {
    return new ArrayList<>(properties);
  }

  @Override
  public Long getTextualLineCount() {
    return textualLineCount;
  }

  @Override
  public long getFullContentOctets() {
    return contentOctets;
  }

  protected int getBodyStartOctet() {
    return bodyStartOctet;
  }

  @Override
  public Date getInternalDate() {
    return internalDate;
  }

  @Override
  @Transient
  public JPAId getMailboxId() {
    return getMailbox().getMailboxId();
  }

  @Override
  public MessageUid getUid() {
    return MessageUid.of(uid);
  }

  @Override
  public boolean isAnswered() {
    return answered;
  }

  @Override
  public boolean isDeleted() {
    return deleted;
  }

  @Override
  public boolean isDraft() {
    return draft;
  }

  @Override
  public boolean isFlagged() {
    return flagged;
  }

  @Override
  public boolean isRecent() {
    return recent;
  }

  @Override
  public boolean isSeen() {
    return seen;
  }

  @Override
  public void setUid(MessageUid uid) {
    this.uid = uid.asLong();
  }

  @Override
  public long getHeaderOctets() {
    return bodyStartOctet;
  }

  @Override
  public void setFlags(Flags flags) {
    answered = flags.contains(Flags.Flag.ANSWERED);
    deleted = flags.contains(Flags.Flag.DELETED);
    draft = flags.contains(Flags.Flag.DRAFT);
    flagged = flags.contains(Flags.Flag.FLAGGED);
    recent = flags.contains(Flags.Flag.RECENT);
    seen = flags.contains(Flags.Flag.SEEN);

    String[] userflags = flags.getUserFlags();
    userFlags.clear();
    for (String userflag : userflags) {
      userFlags.add(new JamesUserFlag(userflag));
    }
  }

  @Override
  public Flags createFlags() {
    return FlagsFactory.createFlags(this, createUserFlags());
  }

  protected String[] createUserFlags() {
    return userFlags.stream().map(JamesUserFlag::getName).toArray(String[]::new);
  }

  @Override
  public InputStream getFullContent() throws IOException {
    return new SequenceInputStream(getHeaderContent(), getBodyContent());
  }

  @Override
  public long getBodyOctets() {
    return getFullContentOctets() - getBodyStartOctet();
  }

  @Override
  public MessageId getMessageId() {
    return new DefaultMessageId();
  }

  @Override
  public int compareTo(MailboxMessage other) {
    return MESSAGE_UID_COMPARATOR.compare(this, other);
  }

  @Override
  public List<MessageAttachment> getAttachments() {
    try {
      return new MessageParser().retrieveAttachments(getFullContent());
    } catch (MimeException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
