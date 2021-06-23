package cn.asany.security.core.bean;

import cn.asany.base.common.Ownership;
import cn.asany.security.core.bean.databind.RolesDeserializer;
import cn.asany.security.core.bean.enums.UserType;
import cn.asany.security.core.validators.UsernameCannotRepeatValidator;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.spring.validation.Use;
import org.jfantasy.framework.util.common.ClassUtil;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;

/**
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTH_USER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user_groups", "website", "menus", "authorities"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseBusEntity implements Ownership {

    private static final long serialVersionUID = 5507435998232223911L;

    public static final String OWNERSHIP_KEY = "PERSONAL";

    @Transient
    private String code;

    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(generator = "auth_user_gen")
    @TableGenerator(name = "auth_user_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "auth_user:id", valueColumnName = "gen_value")
    private Long id;

    /**
     * 用户登录名称
     */
    @NotEmpty(groups = {RESTful.POST.class, RESTful.PUT.class})
    @Length(min = 6, max = 20, groups = {RESTful.POST.class, RESTful.PUT.class})
    @Use(vali = UsernameCannotRepeatValidator.class, groups = {RESTful.POST.class})
    @Column(name = "USERNAME", length = 20, updatable = false, nullable = false, unique = true)
    private String username;
    /**
     * 登录密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "PASSWORD", length = 50, nullable = false)
    private String password;

    /**
     * 用户类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE", length = 20, nullable = false)
    private UserType userType;
    /**
     * 用户显示昵称
     */
    @Column(name = "NICK_NAME", length = 50)
    private String nickName;
    /**
     * 是否启用
     */
    @Column(name = "ENABLED")
    private Boolean enabled;
    /**
     * 未过期
     */
    @Column(name = "NON_EXPIRED")
    private Boolean accountNonExpired;
    /**
     * 未锁定
     */
    @Column(name = "NON_LOCKED")
    private Boolean accountNonLocked;
    /**
     * 未失效
     */
    @Column(name = "CREDENTIALS_NON_EXPIRED")
    private Boolean credentialsNonExpired;
    /**
     * 锁定时间
     */
    @Column(name = "LOCK_TIME")
    private Date lockTime;
    /**
     * 最后登录时间
     */
    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;
    /**
     * 用户对应的角色
     */
    @JsonDeserialize(using = RolesDeserializer.class)
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_USER", joinColumns = @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_AUTH_ROLE_USER_USER")), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"), foreignKey = @ForeignKey(name = "FK_ROLE_USER_UID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Role> roles;

    /**
     * 管理人员
     */
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "FK_AUTH_USER_EMPLOYEE"))
//    private Employee employee;

    /**
     * 所属组织
     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_USER_ORGANIZATION"), updatable = false, nullable = false)
//    private Organization organization;

    /**
     * 用户权限
     */
    @Transient
    private List<GrantPermission> grants;


    /**
     * 扩展字段
     */
    @Convert(converter = MapConverter.class)
    @Column(name = "PROPERTIES", columnDefinition = "Text")
    private Map<String, Object> properties;

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

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", nickName='" + nickName + '\'' +
            ", enabled=" + enabled +
            ", accountNonExpired=" + accountNonExpired +
            ", accountNonLocked=" + accountNonLocked +
            ", credentialsNonExpired=" + credentialsNonExpired +
            ", lockTime=" + lockTime +
            ", lastLoginTime=" + lastLoginTime +
            '}';
    }

    @JsonIgnore
    public Set<String> getAuthoritys() {
        Set<String> authoritys = new HashSet<>();
//        authoritys.add(SecurityScope.newInstance(SecurityType.user, this.getId().toString()).toString());
//        // 添加登录账户对应的组织，如果为 employee 时，忽略该组织
//        if (!"employee".equals(this.userType.name())) {
//            authoritys.add(SecurityScope.newInstance(SecurityType.organization, this.getOrganization().getId()).toString());
//        }
//        // 添加登录账户分配的角色
//        authoritys.addAll(this.getRoles().stream().map(role -> SecurityScope.newInstance(SecurityType.role, role.getId()).toString()).collect(Collectors.toList()));
        return authoritys;
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