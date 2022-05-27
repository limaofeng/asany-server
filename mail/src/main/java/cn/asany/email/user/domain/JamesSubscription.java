package cn.asany.email.user.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.james.mailbox.store.user.model.Subscription;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "Subscription")
@Table(
    name = "JAMES_SUBSCRIPTION",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"USER_NAME", "MAILBOX_NAME"},
            name = "UK_JAMES_SUBSCRIPTION_USER_MAILBOX"))
public class JamesSubscription implements Subscription {

  private static final String TO_STRING_SEPARATOR = "  ";

  /** Primary key */
  @GeneratedValue
  @Id
  @Column(name = "SUBSCRIPTION_ID")
  private long id;

  /** Name of the subscribed user */
  @Basic(optional = false)
  @Column(name = "USER_NAME", nullable = false, length = 100)
  private String username;

  /** Subscribed mailbox */
  @Basic(optional = false)
  @Column(name = "MAILBOX_NAME", nullable = false, length = 100)
  private String mailbox;

  @Override
  public String getMailbox() {
    return mailbox;
  }

  @Override
  public String getUser() {
    return username;
  }

  public JamesSubscription(String username, String mailbox) {
    super();
    this.username = username;
    this.mailbox = mailbox;
  }

  @Override
  public String toString() {
    return "Subscription ( "
        + "id = "
        + this.id
        + TO_STRING_SEPARATOR
        + "user = "
        + this.username
        + TO_STRING_SEPARATOR
        + "mailbox = "
        + this.mailbox
        + TO_STRING_SEPARATOR
        + " )";
  }
}
