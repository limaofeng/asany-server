package cn.asany.email.mailbox.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.apache.james.mailbox.store.mail.model.Property;
import org.apache.openjpa.persistence.jdbc.Index;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 属性
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "MailProperty")
@Table(
    name = "JAMES_MAIL_PROPERTY",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_LOCAL_NAME",
            columnNames = {"MAILBOX_ID", "LOCAL_NAME"}))
public class JamesProperty implements Property, Serializable {

  /** The system unique key */
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "MAILBOX_ID")
  private long mailboxId;

  @Column(name = "MAIL_UID")
  private long uid;

  /** Order within the list of properties */
  @Basic(optional = false)
  @Column(name = "LINE_NUMBER", nullable = false)
  @Index(name = "INDEX_PROPERTY_LINE_NUMBER")
  private int line;

  /** Local part of the name of this property */
  @Basic(optional = false)
  @Column(name = "LOCAL_NAME", nullable = false, length = 500)
  private String localName;

  /** Namespace part of the name of this property */
  @Basic(optional = false)
  @Column(name = "NAME_SPACE", nullable = false, length = 500)
  private String namespace;

  /** Value of this property */
  @Basic(optional = false)
  @Column(name = "VALUE", nullable = false, length = 1024)
  private String value;

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
      foreignKey = @ForeignKey(name = "FK_JAMES_MAIL_PROPERTIES"))
  private JamesMailboxMessage mailboxMessage;

  public JamesProperty(JamesProperty property) {
    this(
        property.getNamespace(), property.getLocalName(), property.getValue(), property.getOrder());
  }

  public JamesProperty(Property property, int order) {
    this(property.getNamespace(), property.getLocalName(), property.getValue(), order);
  }

  public JamesProperty(String namespace, String localName, String value, int order) {
    super();
    this.localName = localName;
    this.namespace = namespace;
    this.value = value;
    this.line = order;
  }

  public int getOrder() {
    return line;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    JamesProperty that = (JamesProperty) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
