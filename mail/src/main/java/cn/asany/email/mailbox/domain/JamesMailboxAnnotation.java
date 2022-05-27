package cn.asany.email.mailbox.domain;

import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "MailboxAnnotation")
@Table(name = "JAMES_MAILBOX_ANNOTATION")
@IdClass(JPAMailboxAnnotationId.class)
public class JamesMailboxAnnotation {

  public static final String MAILBOX_ID = "MAILBOX_ID";
  public static final String ANNOTATION_KEY = "ANNOTATION_KEY";
  public static final String VALUE = "VALUE";

  @Id
  @Column(name = MAILBOX_ID)
  private Long mailboxId;

  @Id
  @Column(name = ANNOTATION_KEY, length = 200)
  private String key;

  @Basic()
  @Column(name = VALUE)
  private String value;

  public JamesMailboxAnnotation(long mailboxId, String key, String value) {
    this.mailboxId = mailboxId;
    this.key = key;
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    JamesMailboxAnnotation that = (JamesMailboxAnnotation) o;
    return mailboxId != null
        && Objects.equals(mailboxId, that.mailboxId)
        && key != null
        && Objects.equals(key, that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailboxId, key);
  }
}
