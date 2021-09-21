package cn.asany.security.core.bean;

import cn.asany.organization.core.bean.Organization;
import cn.asany.security.core.bean.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import lombok.*;
import net.bytebuddy.description.modifier.Ownership;
import org.hibernate.annotations.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 角色
 *
 * @author limaofeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "AUTH_ROLE")
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
  "menus",
  "permissions",
  "users",
  "roleAuthorities"
})
public class Role extends BaseBusEntity {

  public static final Role USER = Role.builder().id(1L).code("USER").name("普通用户").build();
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
  private List<User> users;
  /** 角色类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private RoleType type;

  /** 所有者 */
  @Any(
      metaColumn =
          @Column(name = "OWNERSHIP_TYPE", length = 10, insertable = false, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {
        @MetaValue(targetEntity = User.class, value = User.OWNERSHIP_KEY),
        @MetaValue(targetEntity = Organization.class, value = Organization.OWNERSHIP_KEY)
      })
  @JoinColumn(name = "OWNERSHIP_ID", insertable = false, updatable = false)
  private Ownership ownership;
}
