package cn.asany.security.core.bean;

import cn.asany.organization.core.bean.Organization;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author liumeng @Description: (这里用一句话描述这个类的作用)
 * @date 14:34 2020-04-22
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_ROLE_SPACE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoleSpace extends BaseBusEntity {
  @Id
  @Column(name = "ID", length = 50)
  private String id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_ROLE_SPACE"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private Organization organization;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    RoleSpace roleSpace = (RoleSpace) o;
    return id != null && Objects.equals(id, roleSpace.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
