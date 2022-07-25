package cn.asany.security.core.service;

import cn.asany.base.common.domain.enums.EmailStatus;
import cn.asany.base.common.domain.enums.PhoneNumberStatus;
import cn.asany.security.core.dao.GrantPermissionDao;
import cn.asany.security.core.dao.UserDao;
import cn.asany.security.core.domain.Role;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.domain.enums.UserType;
import cn.asany.security.core.exception.UserInvalidException;
import cn.asany.security.core.exception.UserNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.LoginUser.LoginUserBuilder;
import org.jfantasy.framework.security.core.userdetails.UserDetails;
import org.jfantasy.framework.security.core.userdetails.UserDetailsService;
import org.jfantasy.framework.security.core.userdetails.UsernameNotFoundException;
import org.jfantasy.framework.security.crypto.password.PasswordEncoder;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.reflect.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/** @author limaofeng */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements UserDetailsService {

  public static final String LOGIN_ATTRS_AVATAR = "_avatar";

  public static final String LOGIN_ATTRS_NICKNAME = "_nickName";

  private final UserDao userDao;

  protected final MessageSourceAccessor messages;

  @Autowired private GrantPermissionDao grantPermissionDao;

  private PasswordEncoder passwordEncoder;

  private RoleService roleService;

  @Autowired
  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  @Autowired(required = false)
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public UserService(UserDao userDao, MessageSourceAccessor messages) {
    this.userDao = userDao;
    this.messages = messages;
  }

  /**
   * 保存用户
   *
   * @param user 用户对象
   */
  public User save(User user) {
    boolean existing =
        this.userDao.exists(PropertyFilter.builder().equal("username", user.getUsername()).build());
    if (existing) {
      throw new ValidationException("登录名[" + user.getUsername() + "]已经存在");
    }

    // 默认昵称与用户名一致
    if (StringUtil.isBlank(user.getNickName())) {
      user.setNickName(user.getUsername());
    }

    // 默认为 USER
    if (user.getUserType() == null) {
      user.setUserType(UserType.USER);
    }

    // 初始化用户权限
    if (user.getRoles() == null) {
      user.setRoles(new ArrayList<>());
    }

    if (UserType.ADMIN == user.getUserType()) {
      user.getRoles().add(Role.ADMIN);
    } else {
      user.getRoles().add(Role.USER);
    }

    // 初始化用户状态
    user.setEnabled(true);
    user.setAccountNonLocked(true);
    user.setAccountNonExpired(true);
    user.setCredentialsNonExpired(true);

    if (StringUtil.isNotBlank(user.getPassword())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    // 保存用户
    return this.userDao.save(user);
  }

  /**
   * 修改密码
   *
   * @param id 用户ID
   * @param oldPassword 旧密码
   * @param newPassword 新密码
   */
  public void changePassword(Long id, String oldPassword, String newPassword) {
    User user = this.userDao.getReferenceById(id);
    if (user.getId() == null) {
      throw new UserNotFoundException("用户不存在");
    }
    if (!user.getEnabled()) {
      throw new UserInvalidException("用户已被禁用");
    }
    if (!this.passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new ValidationException("100106", "提供的 password token 不正确!");
    }
    user.setPassword(passwordEncoder.encode(newPassword));
    this.userDao.save(user);
  }

  public Optional<User> findOneByUsername(String username) {
    return this.userDao.findOne(Example.of(User.builder().username(username).build()));
  }

  public User findUniqueByUsername(String username) {
    Optional<User> optional =
        this.userDao.findOne(Example.of(User.builder().username(username).build()));
    return optional.orElse(null);
  }

  public Page<User> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return this.userDao.findPage(pageable, filters);
  }

  public void delete(Long... ids) {
    this.delete(Arrays.stream(ids).collect(Collectors.toSet()));
  }

  public void delete(Set<Long> ids) {
    this.userDao.deleteAllByIdInBatch(ids);
  }

  public Optional<User> get(Long id) {
    return this.userDao.findById(id);
  }

  public User update(Long id, User user) {
    user.setId(id);
    User oldUser = this.userDao.getReferenceById(id);
    BeanUtil.copyProperties(
        oldUser,
        user,
        (Property property, Object value, Object _dest) -> {
          if ("avatar".equals(property.getName())) {
            return true;
          }
          return value != null;
        });
    user = this.userDao.update(oldUser);
    return user;
  }

  public List<Role> addRoles(Long id, String[] roles, boolean clear) {
    User user = this.userDao.getReferenceById(id);
    if (clear) {
      user.getRoles().clear();
    }
    for (String role : roles) {
      if (ObjectUtil.exists(user.getRoles(), "code", role)) {
        continue;
      }
      Optional<Role> optionalRole = this.roleService.findByCode(role);
      if (!optionalRole.isPresent()) {
        continue;
      }
      user.getRoles().add(optionalRole.get());
    }
    this.userDao.save(user);
    return user.getRoles();
  }

  public List<Role> removeRoles(Long id, String... roles) {
    User user = this.userDao.getOne(id);
    for (String role : roles) {
      ObjectUtil.remove(user.getRoles(), "id", role);
    }
    this.userDao.save(user);
    return user.getRoles();
  }

  public PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }

  public User findUniqueById(Long uid) {
    return null;
  }

  public User findById(Long id) {
    return userDao.findById(id).orElse(null);
  }

  // 检查用户是否有指定的权限
  public String checkUserPermissions(long id, String permissions) {
    User user = this.userDao.getOne(id);
    String[] permisstionArray = permissions.split(",");
    // 检查结果出用户拥有的权限列表
    //        UserServiceUtil.setDepartmentDao(departmentDao);
    UserServiceUtil.setGrantPermissionDao(grantPermissionDao);
    Set<String> hasPermissionsList = UserServiceUtil.hasGrantPermissions(user, permisstionArray);
    // 返回json
    return UserServiceUtil.comparePermissionsResult(hasPermissionsList, permisstionArray);
  }

  public void loginSuccess() {}

  public void loginFailure() {}

  // 获取用户角色for Authoritys
  public Set<String> getUserRoleCodesAuthoritys(User user) {
    Set<String> authoritys = new HashSet<>();
    try {
      // 取用户的角色列表
      //            UserServiceUtil.setDepartmentDao(departmentDao);
      UserServiceUtil.setGrantPermissionDao(grantPermissionDao);
      Set<String> roleCodes = UserServiceUtil.getUserRoleCodes(user);
      if (!CollectionUtils.isEmpty(roleCodes)) {
        //                for (String roleCode : roleCodes) {
        //                    authoritys.add(SecurityScope.newInstance(SecurityType.role,
        // roleCode).toString());
        //                }
      }
    } catch (Exception e) {
      log.error("getUserRoleCodes exception", e);
    }
    return authoritys;
  }

  public User findByEmployee(Long employeeId) {
    //        User byEmployee = userDao.findByEmployee(Employee.builder().id(employeeId).build());
    //        return byEmployee;
    return null;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optional =
        this.userDao.findOne(Example.of(User.builder().username(username).build()));
    // 用户不存在
    if (!optional.isPresent()) {
      throw new UsernameNotFoundException(
          this.messages.getMessage(
              "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }
    return buildLoginUser(optional.get());
  }

  public LoginUser buildLoginUser(User user) {
    LoginUserBuilder builder =
        LoginUser.builder()
            .uid(user.getId())
            .type(user.getUserType().name())
            .username(user.getUsername())
            .title(user.getTitle())
            .name(user.getNickName())
            .password(user.getPassword())
            .enabled(user.getEnabled())
            .accountNonExpired(user.getAccountNonExpired())
            .accountNonLocked(user.getAccountNonLocked())
            .credentialsNonExpired(user.getCredentialsNonExpired())
            .authorities(user.getAuthorities());

    if (user.getPhone() != null && user.getPhone().getStatus() == PhoneNumberStatus.VERIFIED) {
      builder.phone(user.getPhone().getNumber());
    }

    if (user.getEmail() != null && user.getEmail().getStatus() == EmailStatus.VERIFIED) {
      builder.phone(user.getEmail().getAddress());
    }

    LoginUser loginUser = builder.build();
    loginUser.setAttribute(LOGIN_ATTRS_AVATAR, user.getAvatar());
    return loginUser;
  }
}
