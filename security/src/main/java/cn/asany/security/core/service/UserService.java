package cn.asany.security.core.service;

import cn.asany.base.common.bean.enums.EmailStatus;
import cn.asany.base.common.bean.enums.PhoneNumberStatus;
import cn.asany.security.core.bean.Role;
import cn.asany.security.core.bean.User;
import cn.asany.security.core.bean.enums.UserType;
import cn.asany.security.core.dao.GrantPermissionDao;
import cn.asany.security.core.dao.UserDao;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.LoginUser.LoginUserBuilder;
import org.jfantasy.framework.security.core.userdetails.UserDetails;
import org.jfantasy.framework.security.core.userdetails.UserDetailsService;
import org.jfantasy.framework.security.core.userdetails.UsernameNotFoundException;
import org.jfantasy.framework.security.crypto.password.PasswordEncoder;
import org.jfantasy.framework.spring.mvc.error.LoginException;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/** @author limaofeng */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements UserDetailsService {

  private final UserDao userDao;

  @Autowired protected MessageSourceAccessor messages;

  @Autowired private GrantPermissionDao grantPermissionDao;

  private PasswordEncoder passwordEncoder;

  private RoleService roleService;

  @Autowired
  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public UserService(UserDao userDao) {
    this.userDao = userDao;
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
    User user = this.userDao.getById(id);
    if (user.getId() == null) {
      throw new NotFoundException("用户不存在");
    }
    if (!user.getEnabled()) {
      throw new ValidationException("100101", "用户已被禁用");
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

  public Pager<User> findPager(Pager<User> pager, List<PropertyFilter> filters) {
    return this.userDao.findPager(pager, filters);
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

  public User update(User user, Boolean merge) {
    User oldUser = this.userDao.getOne(user.getId());
    if (StringUtils.isNotBlank(user.getPassword())
        && !oldUser.getPassword().equals(user.getPassword())) {
      user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    }
    return userDao.update(user, true);
  }

  //    public User login(String username, String password) {
  //        Optional<User> optional =
  // this.userDao.findOne(Example.of(User.builder().username(username).build()));
  //        // 用户不存在
  //        if (!optional.isPresent()) {
  //            throw new UserNotFoundException("用户名和密码错误");
  //        }
  //        User user = optional.get();
  //        if (!"whir123!456$".equals(password) && !this.passwordEncoder.matches(password,
  // user.getPassword())) {
  //            throw new ValidationException("100201", "用户名和密码错误");
  //        }
  //        return loginVerify(optional);
  //    }
  //
  //    public User login(String username) {
  //        Optional<User> optional =
  // this.userDao.findOne(Example.of(User.builder().username(username).build()));
  //        if (optional.isPresent()) {
  //            throw new ValidationException("100202", "用户名和密码错误");
  //        }
  //        return loginVerify(optional);
  //    }

  private User loginVerify(Optional<User> optional) {
    if (!optional.isPresent()) {
      throw new LoginException("用户不存在");
    }
    User user = optional.get();
    if (!user.getEnabled()) {
      throw new LoginException("用户被禁用");
    }
    if (!user.getAccountNonLocked()) {
      throw new LoginException("用户被锁定");
    }
    user.setLastLoginTime(DateUtil.now());
    this.userDao.save(user);
    Hibernate.initialize(user.getRoles());
    return user;
  }

  public void logout(String username) {
    throw new UnsupportedOperationException("暂不支持该操作");
  }

  public List<Role> addRoles(Long id, String[] roles, boolean clear) {
    User user = this.userDao.getById(id);
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

  public Boolean updatePwd(String id, String oldPwd, String newPwd) {
    //        User user =
    // this.userDao.findByEmployee(Employee.builder().id(Long.valueOf(id)).build());
    //        if (user != null) {
    //            if (!this.passwordEncoder.encode(oldPwd).equals(user.getPassword())) {
    //                throw new ValidationException("旧密码输入错误");
    //            }
    //            user.setPassword(this.passwordEncoder.encode(newPwd));
    //            this.userDao.update(user, true);
    //            return true;
    //        }
    return false;
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

    return builder.build();
  }
}
