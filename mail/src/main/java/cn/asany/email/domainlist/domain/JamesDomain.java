package cn.asany.email.domainlist.domain;

import cn.asany.organization.core.domain.Organization;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.apache.james.core.Domain;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * James Domain
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "JamesDomain")
@Table(name = "JAMES_DOMAIN")
public class JamesDomain extends BaseBusEntity {

  /** The name of the domain. column name is chosen to be compatible with the JDBCDomainList. */
  @Id
  @Column(name = "DOMAIN_NAME", nullable = false, length = 100)
  private String name;

  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_JAMES_DOMAIN_ORG"))
  private Organization organization;

  /**
   * Use this simple constructor to create a new Domain.
   *
   * @param name the name of the Domain
   */
  public JamesDomain(Domain name) {
    this.name = name.asString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    JamesDomain that = (JamesDomain) o;
    return name != null && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
