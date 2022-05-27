package cn.asany.email.mailbox.domain;

import cn.asany.email.mailbox.component.JPAId;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.apache.james.mailbox.model.Mailbox;
import org.apache.james.mailbox.model.MailboxPath;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * James Mailbox
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "Mailbox")
@Table(
    name = "JAMES_MAILBOX",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_MAILBOX_NAME",
            columnNames = {"NAMESPACE", "NAME", "USER_NAME"}))
public class JamesMailbox extends BaseBusEntity {

  private static final String TAB = " ";

  public static JamesMailbox from(Mailbox mailbox) {
    return new JamesMailbox(mailbox);
  }

  /** The value for the mailboxId field */
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** The value for the name field */
  @Basic(optional = false)
  @Column(name = "NAME", nullable = false, length = 200)
  private String name;

  /** The value for the uidValidity field */
  @Basic(optional = false)
  @Column(name = "UID_VALIDITY", nullable = false)
  private long uidValidity;

  @Basic(optional = true)
  @Column(name = "USER_NAME", nullable = true, length = 200)
  private String user;

  @Basic(optional = false)
  @Column(name = "NAMESPACE", nullable = false, length = 200)
  private String namespace;

  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;

  @Basic(optional = false)
  @Column(name = "LAST_UID", nullable = true)
  private long lastUid;

  @Basic(optional = false)
  @Column(name = "HIGHEST_MODSEQ", nullable = true)
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
    return new Mailbox(generateAssociatedPath(), uidValidity, new JPAId(id));
  }

  public JPAId getMailboxId() {
    return JPAId.of(id);
  }

  @Override
  public String toString() {
    return "Mailbox ( "
        + "id = "
        + this.id
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
    return id != null && Objects.equals(id, that.id);
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
