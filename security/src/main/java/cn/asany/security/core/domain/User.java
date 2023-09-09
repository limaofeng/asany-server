package cn.asany.security.core.domain;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.Email;
import cn.asany.base.common.domain.Phone;
import cn.asany.security.core.domain.enums.Sex;
import cn.asany.security.core.domain.enums.UserType;
import cn.asany.security.core.validators.UsernameCannotRepeatValidator;
import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.*;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.Tenantable;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.spring.validation.Operation;
import org.jfantasy.framework.spring.validation.Use;

/**
 * 用户
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(
    name = "AUTH_USER",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_AUTH_USER_PHONE",
          columnNames = {"USER_TYPE", "PHONE_NUMBER"}),
      @UniqueConstraint(
          name = "UK_AUTH_USER_USERNAME",
          columnNames = {"USERNAME"})
    })
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseBusEntity implements Ownership, Tenantable {

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
  @Column(name = "USERNAME", length = 20, updatable = false, nullable = false)
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
  @Type(type = "file")
  @Column(name = "avatar", precision = 500)
  private FileObject avatar;

  /** 用户显示昵称 */
  @Column(name = "NICK_NAME", length = 50)
  private String nickname;

  /** 用户显示昵称 */
  @Column(name = "TITLE", length = 50)
  private String title;

  /** 用户状态 */
  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "USER_ID")
  @ToString.Exclude
  private UserStatus status;

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

  /** 登陆成功后需要重置密码 */
  @Builder.Default
  @Column(name = "FORCE_PASSWORD_RESET")
  private Boolean forcePasswordReset = Boolean.FALSE;

  /** 锁定时间 */
  @Column(name = "LOCK_TIME")
  private Date lockTime;

  /** 最后登录时间 */
  @Column(name = "LAST_LOGIN_TIME")
  private Date lastLoginTime;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24)
  private String tenantId;

  /** 用户权限 */
  @Transient private List<Permission> permissions;

  @Transient
  public Set<GrantedAuthority> getAuthorities() {
    //    Set<GrantedAuthority> authorities = new HashSet<>();
    //    authorities.addAll(
    //        this.roles.stream()
    //            .map(item -> SimpleGrantedAuthority.newInstance("ROLE_" + item.getName()))
    //            .collect(Collectors.toList()));
    //    //    authorities.addAll(
    //    //      this.groups.stream()
    //    //        .map(item -> SimpleGrantedAuthority.newInstance("GROUP_" + item.getId()))
    //    //            .collect(Collectors.toList()));
    return new HashSet<>();
  }

  @Override
  public String getName() {
    return this.nickname;
  }

  @Override
  @Transient
  public String getOwnerType() {
    return OWNERSHIP_KEY;
  }

  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return getId() != null && Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
