package cn.asany.security.core.bean;

import cn.asany.organization.core.bean.Organization;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
@Table(
    name = "AUTH_ROLE_SPACE",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"CODE", "ORGANIZATION_ID"},
            name = "UK_AUTH_ROLE_SPACE_CODE"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoleSpace extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "CODE", length = 32)
  private String code;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
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
