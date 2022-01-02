package cn.asany.email.mailbox.bean;

import javax.persistence.*;
import lombok.Data;
import org.apache.james.mailbox.store.mail.model.Property;
import org.apache.openjpa.persistence.jdbc.Index;

@Data
@Entity(name = "Property")
@Table(name = "JAMES_MAIL_PROPERTY")
public class JamesProperty implements Property {

  /** The system unique key */
  @Id
  @GeneratedValue
  @Column(name = "PROPERTY_ID", nullable = true)
  private long id;

  /** Order within the list of properties */
  @Basic(optional = false)
  @Column(name = "PROPERTY_LINE_NUMBER", nullable = false)
  @Index(name = "INDEX_PROPERTY_LINE_NUMBER")
  private int line;

  /** Local part of the name of this property */
  @Basic(optional = false)
  @Column(name = "PROPERTY_LOCAL_NAME", nullable = false, length = 500)
  private String localName;

  /** Namespace part of the name of this property */
  @Basic(optional = false)
  @Column(name = "PROPERTY_NAME_SPACE", nullable = false, length = 500)
  private String namespace;

  /** Value of this property */
  @Basic(optional = false)
  @Column(name = "PROPERTY_VALUE", nullable = false, length = 1024)
  private String value;

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
}
