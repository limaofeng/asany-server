package cn.asany.email.mailbox.bean;

import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

/** @author limaofeng */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "UserFlag")
@Table(name = "JAMES_MAIL_USERFLAG")
public class JamesUserFlag {

  @Id
  @GeneratedValue
  @Column(name = "USERFLAG_ID", nullable = true)
  private Long id;

  @Basic(optional = false)
  @Column(name = "USERFLAG_NAME", nullable = false, length = 500)
  private String name;

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
