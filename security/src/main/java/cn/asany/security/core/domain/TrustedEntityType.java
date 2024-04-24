package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.Hibernate;

/**
 * 角色命名空间
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_TRUSTED_ENTITY_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TrustedEntityType extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 32)
  private String id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    TrustedEntityType trustedEntityType = (TrustedEntityType) o;
    return id != null && Objects.equals(id, trustedEntityType.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
