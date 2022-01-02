package cn.asany.email.mailbox.bean;

import cn.asany.email.mailbox.component.JPAId;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.apache.james.mailbox.model.Mailbox;
import org.apache.james.mailbox.model.MailboxPath;
import org.hibernate.Hibernate;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "Mailbox")
@Table(name = "JAMES_MAILBOX")
public class JamesMailbox {

  private static final String TAB = " ";

  public static JamesMailbox from(Mailbox mailbox) {
    return new JamesMailbox(mailbox);
  }

  /** The value for the mailboxId field */
  @Id
  @GeneratedValue
  @Column(name = "MAILBOX_ID")
  private Long mailboxId;

  /** The value for the name field */
  @Basic(optional = false)
  @Column(name = "MAILBOX_NAME", nullable = false, length = 200)
  private String name;

  /** The value for the uidValidity field */
  @Basic(optional = false)
  @Column(name = "MAILBOX_UID_VALIDITY", nullable = false)
  private long uidValidity;

  @Basic(optional = true)
  @Column(name = "USER_NAME", nullable = true, length = 200)
  private String user;

  @Basic(optional = false)
  @Column(name = "MAILBOX_NAMESPACE", nullable = false, length = 200)
  private String namespace;

  @Basic(optional = false)
  @Column(name = "MAILBOX_LAST_UID", nullable = true)
  private long lastUid;

  @Basic(optional = false)
  @Column(name = "MAILBOX_HIGHEST_MODSEQ", nullable = true)
  private long highestModSeq;

  public JamesMailbox(Mailbox mailbox) {
    this(mailbox.generateAssociatedPath(), mailbox.getUidValidity());
  }

  public JamesMailbox(MailboxPath path, long uidValidity) {
    this.name = path.getName();
    this.user = path.getUser();
    this.namespace = path.getNamespace();
    this.uidValidity = uidValidity;
  }

  public MailboxPath generateAssociatedPath() {
    return new MailboxPath(namespace, user, name);
  }

  public Mailbox toMailbox() {
    return new Mailbox(generateAssociatedPath(), uidValidity, new JPAId(mailboxId));
  }

  public JPAId getMailboxId() {
    return JPAId.of(mailboxId);
  }

  @Override
  public String toString() {
    return "Mailbox ( "
        + "mailboxId = "
        + this.mailboxId
        + TAB
        + "name = "
        + this.name
        + TAB
        + "uidValidity = "
        + this.uidValidity
        + TAB
        + " )";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    JamesMailbox that = (JamesMailbox) o;
    return mailboxId != null && Objects.equals(mailboxId, that.mailboxId);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public long consumeUid() {
    return ++lastUid;
  }

  public long consumeModSeq() {
    return ++highestModSeq;
  }
}
