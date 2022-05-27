package cn.asany.security.core.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 权限分类表 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_PERMISSION_TYPE")
public class PermissionType extends BaseBusEntity {

  private static final long serialVersionUID = 2224908963065749111L;

  /** 未知分类存值 */
  public static final String UNKNOWN = "UNKNOWN";

  /** 权限分类编码 */
  @Id
  @Column(name = "ID", length = 50)
  private String id;
  /** 权限分类名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;
  /** 对应的权限定义 */
  @OneToMany(
      mappedBy = "type",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @ToString.Exclude
  private List<Permission> permissions;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    PermissionType that = (PermissionType) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
