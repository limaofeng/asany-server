package cn.asany.security.core.bean;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.bean.Email;
import cn.asany.base.common.bean.Phone;
import cn.asany.security.core.bean.enums.Sex;
import cn.asany.security.core.bean.enums.UserType;
import cn.asany.security.core.validators.UsernameCannotRepeatValidator;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.core.SimpleGrantedAuthority;
import org.jfantasy.framework.spring.validation.Operation;
import org.jfantasy.framework.spring.validation.Use;

/** @author limaofeng */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_USER")
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
  "user_groups",
  "website",
  "menus",
  "authorities"
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseBusEntity implements Ownership {

  public static final String OWNERSHIP_KEY = "PERSONAL";

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 用户登录名称 */
  @NotEmpty(groups = {Operation.Create.class, Operation.Update.class})
  @Length(
      min = 6,
      max = 20,
      groups = {Operation.Create.class, Operation.Update.class})
  @Use(
      value = UsernameCannotRepeatValidator.class,
      groups = {Operation.Create.class})
  @Column(name = "USERNAME", length = 20, updatable = false, nullable = false, unique = true)
  private String username;
  /** 登录密码 */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "PASSWORD", length = 50, nullable = false)
  private String password;

  /** 用户类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "USER_TYPE", length = 20, nullable = false)
  private UserType userType;
  /** 头像 */
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "avatar", precision = 500)
  private FileObject avatar;
  /** 用户显示昵称 */
  @Column(name = "NICK_NAME", length = 50)
  private String nickName;
  /** 用户显示昵称 */
  @Column(name = "TITLE", length = 50)
  private String title;
  /** 电话 */
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "status", column = @Column(name = "PHONE_STATUS")),
    @AttributeOverride(name = "number", column = @Column(name = "PHONE_NUMBER")),
  })
  private Phone phone;
  /** 邮箱 */
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "status", column = @Column(name = "EMAIL_STATUS")),
    @AttributeOverride(name = "address", column = @Column(name = "EMAIL_ADDRESS")),
  })
  private Email email;
  /** 生日 */
  @Column(name = "BIRTHDAY")
  @Temporal(TemporalType.DATE)
  private Date birthday;
  /** 性别 */
  @Enumerated(EnumType.STRING)
  @Column(name = "SEX", length = 10)
  private Sex sex;
  /** 公司 */
  @Column(name = "COMPANY", length = 200)
  private String company;
  /** 位置 */
  @Column(name = "LOCATION", length = 200)
  private String location;
  /** 自我介绍 */
  @Column(name = "BIO", length = 500)
  private String bio;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 未过期 */
  @Column(name = "NON_EXPIRED")
  private Boolean accountNonExpired;
  /** 未锁定 */
  @Column(name = "NON_LOCKED")
  private Boolean accountNonLocked;
  /** 未失效 */
  @Column(name = "CREDENTIALS_NON_EXPIRED")
  private Boolean credentialsNonExpired;
  /** 锁定时间 */
  @Column(name = "LOCK_TIME")
  private Date lockTime;
  /** 最后登录时间 */
  @Column(name = "LAST_LOGIN_TIME")
  private Date lastLoginTime;
  /** 用户对应的角色 */
  @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "AUTH_ROLE_USER",
      joinColumns =
          @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_AUTH_ROLE_USER_USER")),
      inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"),
      foreignKey = @ForeignKey(name = "FK_ROLE_USER_UID"))
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  @ToString.Exclude
  private List<Role> roles;
  /** 用户权限 */
  @Transient private List<GrantPermission> grants;

  @Transient
  public Set<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = new HashSet<>();
    if (this.userType == UserType.ADMIN) {
      authorities.add(SimpleGrantedAuthority.newInstance("ROLE_ADMIN"));
    } else {
      authorities.add(SimpleGrantedAuthority.newInstance("ROLE_USER"));
    }
    return authorities;
  }

  @Override
  public String getName() {
    return this.nickName;
  }

  @Override
  @Transient
  public String getOwnerType() {
    return OWNERSHIP_KEY;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
