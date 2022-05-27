package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.PermissionGrantType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 权限配置信息
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
@Table(name = "AUTH_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Permission extends BaseBusEntity {

  private static final long serialVersionUID = 2224908963065749499L;

  @Id
  @Column(name = "ID", length = 100)
  private String id;
  /** 权限名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 资源描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;
  /** 是否启用 */
  @Column(name = "ENABLED", nullable = false)
  private Boolean enabled;
  /** 允许的授权对象类型 tokenType */
  @ElementCollection
  @CollectionTable(
      name = "AUTH_PERMISSION_TOKEN_TYPE",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_TOKEN_TYPE_PID"),
      joinColumns = @JoinColumn(name = "PERMISSION_ID", nullable = false))
  @Column(name = "TOKEN_TYPE", nullable = false)
  private Set<String> tokenTypes;
  /** 排序字段 */
  @Column(name = "SORT", nullable = false)
  private Integer index;
  /** 权限类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TYPE",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_PERMISSION_TYPE"))
  @ToString.Exclude
  private PermissionType type;
  /** 权限授权的类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "GRANT_TYPE", length = 20, nullable = false)
  private PermissionGrantType grantType;
  /** 资源类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "RESOURCE_TYPE",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_RESOURCE_TYPE"))
  @ToString.Exclude
  private ResourceType resourceType;
  /** 资源 */
  @ElementCollection
  @CollectionTable(
      name = "AUTH_AUTH_PERMISSION_RESOURCE",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_RESOURCE_PID"),
      joinColumns = @JoinColumn(name = "PERMISSION_ID", nullable = false))
  @Column(name = "RESOURCE", nullable = false)
  private Set<String> resources;
  /** 上级权限 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_PERMISSION_PARENT"))
  @ToString.Exclude
  private Permission parent;
  /** 子权限 */
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @OrderBy("index ASC")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  @ToString.Exclude
  private List<Permission> scopes;

  @Transient private List<GrantPermission> grants;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Permission that = (Permission) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
