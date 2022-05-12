package cn.asany.security.core.bean;

import cn.asany.organization.core.bean.Organization;
import cn.asany.security.core.bean.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 角色
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
    name = "AUTH_ROLE",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"CODE", "ORGANIZATION_ID"},
            name = "UK_AUTH_ROLE_CODE_ORGANIZATION"))
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
  "menus",
  "permissions",
  "users",
  "roleAuthorities"
})
public class Role extends BaseBusEntity {

  @Transient
  public static final Role USER = Role.builder().id(1L).code("USER").name("普通用户").build();

  @Transient
  public static final Role ADMIN = Role.builder().id(2L).code("ADMIN").name("管理员").build();

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 角色编码 */
  @Column(name = "CODE", length = 32)
  private String code;
  /** 角色名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 是否启用 0-禁用 1-启用 */
  @Column(name = "ENABLED", nullable = false)
  private Boolean enabled;
  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;
  /**
   * 角色空间<br>
   * 用于区分某一特定业务的角色<br>
   * 如： 内容管理: 内容发布角色, 审核员
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SPACE", foreignKey = @ForeignKey(name = "FK_ROLE_SCOPE"), updatable = false)
  @ToString.Exclude
  private RoleSpace space;
  /** 对应的用户 */
  @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "AUTH_ROLE_USER",
      joinColumns =
          @JoinColumn(
              name = "ROLE_CODE",
              foreignKey = @ForeignKey(name = "FK_AUTH_ROLE_USER_ROLE")),
      inverseJoinColumns = @JoinColumn(name = "USER_ID"),
      foreignKey = @ForeignKey(name = "FK_ROLE_USER_RCODE"))
  @ToString.Exclude
  private List<User> users;
  /** 角色类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private RoleType type;
  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ROLE_ORGANIZATION"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private Organization organization;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Role role = (Role) o;
    return id != null && Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
