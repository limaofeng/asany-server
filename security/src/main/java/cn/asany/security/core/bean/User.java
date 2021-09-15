package cn.asany.security.core.bean;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.bean.Email;
import cn.asany.base.common.bean.Phone;
import cn.asany.security.core.bean.databind.RolesDeserializer;
import cn.asany.security.core.bean.enums.UserType;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.*;
import javax.persistence.*;
import javax.tools.FileObject;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.core.SimpleGrantedAuthority;
import org.jfantasy.framework.util.common.ClassUtil;

/** @author limaofeng */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"password", "properties", "roles"})
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

  @Transient private String code;

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "auth_user_gen")
  @TableGenerator(
      name = "auth_user_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "auth_user:id",
      valueColumnName = "gen_value")
  private Long id;

  /** 用户登录名称 */
  //  @NotEmpty(groups = {Operation.CREATE, Operation.UPDATE})
  //  @Length(
  //      min = 6,
  //      max = 20,
  //      groups = {Operation.CREATE, Operation.UPDATE})
  //  @Use(
  //      value = UsernameCannotRepeatValidator.class,
  //      groups = {Operation.CREATE})
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
  @JsonDeserialize(using = RolesDeserializer.class)
  @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "AUTH_ROLE_USER",
      joinColumns =
          @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_AUTH_ROLE_USER_USER")),
      inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"),
      foreignKey = @ForeignKey(name = "FK_ROLE_USER_UID"))
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<Role> roles;
  /** 扩展字段 */
  @Convert(converter = MapConverter.class)
  @Column(name = "PROPERTIES", columnDefinition = "Text")
  private Map<String, Object> properties;

  /** 用户权限 */
  @Transient private List<GrantPermission> grants;

  @JsonAnySetter
  public void set(String key, Object value) {
    if (this.properties == null) {
      this.properties = new HashMap<>();
    }
    this.properties.put(key, value);
  }

  @Transient
  public String get(String key) {
    if (this.properties == null || !this.properties.containsKey(key)) {
      return null;
    }
    return this.properties.getOrDefault(key, "").toString();
  }

  @Transient
  public <T> T get(String key, Class<T> toClass) {
    String value = this.get(key);
    if (value == null) {
      return null;
    }
    return ClassUtil.newInstance(toClass, value);
  }

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
}
