package cn.asany.email.mailbox.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

/** @author limaofeng */
@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "MailUserFlag")
@Table(name = "JAMES_MAIL_USERFLAG")
public class JamesUserFlag implements Serializable {

  @Id
  @GeneratedValue
  @Column(name = "USERFLAG_ID", nullable = true)
  private Long id;

  @Basic(optional = false)
  @Column(name = "USERFLAG_NAME", nullable = false, length = 500)
  private String name;

  @Column(name = "MAILBOX_ID")
  private long mailboxId;

  @Column(name = "MAIL_UID")
  private long uid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns(
      value = {
        @JoinColumn(
            name = "MAILBOX_ID",
            referencedColumnName = "MAILBOX_ID",
            insertable = false,
            updatable = false),
        @JoinColumn(
            name = "MAIL_UID",
            referencedColumnName = "MAIL_UID",
            insertable = false,
            updatable = false),
      },
      foreignKey = @ForeignKey(name = "FK_JAMES_MAIL_USERFLAGS"))
  private JamesMailboxMessage mailboxMessage;

  public JamesUserFlag(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    JamesUserFlag that = (JamesUserFlag) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
