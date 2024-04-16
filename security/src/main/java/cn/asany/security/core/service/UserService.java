package cn.asany.security.core.service;

import cn.asany.security.core.dao.UserDao;
import cn.asany.security.core.domain.*;
import cn.asany.security.core.domain.enums.GranteeType;
import cn.asany.security.core.domain.enums.UserType;
import cn.asany.security.core.exception.UserInvalidException;
import cn.asany.security.core.exception.UserNotFoundException;
import cn.asany.security.core.util.PasswordGenerator;
import cn.asany.security.core.util.UserUtil;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.core.SimpleGrantedAuthority;
import org.jfantasy.framework.security.crypto.password.PasswordEncoder;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.reflect.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 用户服务
 *
 * @author limaofeng
 */
@Slf4j
@Service
public class UserService {

  private final PasswordGenerator passwordGenerator = new PasswordGenerator();

  private final UserDao userDao;

  protected final MessageSourceAccessor messages;

  @Getter private PasswordEncoder passwordEncoder;

  private final RoleService roleService;

  private final GroupService groupService;

  private final TenantService tenantService;

  @Autowired(required = false)
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public UserService(
      UserDao userDao,
      MessageSourceAccessor messages,
      RoleService roleService,
      GroupService groupService,
      TenantService tenantService) {
    this.userDao = userDao;
    this.messages = messages;
    this.roleService = roleService;
    this.tenantService = tenantService;
    this.groupService = groupService;
  }

  public List<User> saveAll(List<User> users) {
    return users.stream().map(this::save).collect(Collectors.toList());
  }

  /**
   * 保存用户
   *
   * @param user 用户对象
   */
  public User save(User user) {
    boolean existing =
        this.userDao.exists(PropertyFilter.newFilter().equal("username", user.getUsername()));
    if (existing) {
      throw new ValidationException("登录名[" + user.getUsername() + "]已经存在");
    }

    // 默认昵称与用户名一致
    if (StringUtil.isBlank(user.getNickname())) {
      user.setNickname(user.getUsername());
    }

    // 默认为 USER
    if (user.getUserType() == null) {
      user.setUserType(UserType.USER);
    }

    //    // 初始化用户权限
    //    if (user.getRoles() == null) {
    //      user.setRoles(new ArrayList<>());
    //    }
    //
    //    if (UserType.ADMIN == user.getUserType()) {
    //      user.getRoles().add(Role.ADMIN);
    //    } else {
    //      user.getRoles().add(Role.USER);
    //    }

    // 初始化用户状态
    user.setEnabled(true);
    user.setAccountNonLocked(true);
    user.setAccountNonExpired(true);
    user.setCredentialsNonExpired(true);

    AccessControlSettings accessControlSettings =
        tenantService
            .findAccessControlSettings(user.getTenantId())
            .orElseThrow(() -> new ValidationException("租户访问控制设置不存在"));

    PasswordPolicy passwordPolicy = accessControlSettings.getPasswordPolicy();

    String password = user.getPassword();

    if (StringUtil.isBlank(password)) {
      password = passwordGenerator.generatePassword(passwordPolicy, user.getUsername());
    }

    user.setPassword(passwordEncoder.encode(password));

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
    return this.userDao.findOne(PropertyFilter.newFilter().equal("username", username));
  }

  public Optional<User> findOneByPhone(String username) {
    return this.userDao.findOne(PropertyFilter.newFilter().equal("phone.number", username));
  }

  public Page<User> findPage(Pageable pageable, PropertyFilter filter) {
    return this.userDao.findPage(pageable, filter);
  }

  @Transactional(rollbackFor = Exception.class)
  public User delete(Long id) {
    User user = this.userDao.getReferenceById(id);
    this.userDao.delete(user);
    return user;
  }

  @Transactional(rollbackFor = Exception.class)
  public List<User> deleteMany(List<Long> ids) {
    List<User> users =
        ids.stream().map(this.userDao::getReferenceById).collect(Collectors.toList());
    this.userDao.deleteAllInBatch(users);
    return users;
  }

  public void delete(Set<Long> ids) {
    this.userDao.deleteAllByIdInBatch(ids);
  }

  public User get(Long id) {
    return this.userDao.getReferenceById(id);
  }

  public User update(Long id, User user, boolean merge) {
    user.setId(id);
    User oldUser = this.userDao.getReferenceById(id);

    BeanUtil.PropertyFilter propertyFilter =
        (Property property, Object value, Object _dest) -> {
          if (Arrays.asList(new String[] {"username"}).contains(property.getName())) {
            return false;
          }
          if ("avatar".equals(property.getName())) {
            return true;
          }
          if ("password".equals(property.getName())) {
            return StringUtil.isNotBlank(value);
          }
          return value != null;
        };

    BeanUtil.copyProperties(oldUser, user, propertyFilter);

    user = this.userDao.update(oldUser, merge);
    return user;
  }

  public List<GrantedAuthority> getGrantedAuthorities(Long id) {
    List<Role> roles = roleService.findAllByUser(id);
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role : roles) {
      authorities.add(
          SimpleGrantedAuthority.newInstance(GranteeType.ROLE.name() + "_" + role.getName()));
    }
    List<Group> groups = groupService.findAllByUser(id);
    for (Group group : groups) {
      authorities.add(
          SimpleGrantedAuthority.newInstance(GranteeType.GROUP.name() + "_" + group.getName()));
    }
    return authorities;
  }

  public Optional<User> findById(Long id) {
    return userDao.findById(id);
  }

  // 检查用户是否有指定的权限
  public String checkUserPermissions(long id, String permissions) {
    User user = this.userDao.getReferenceById(id);
    String[] permissionArray = permissions.split(",");
    // 检查结果出用户拥有的权限列表
    //        UserServiceUtil.setDepartmentDao(departmentDao);
    //    UserServiceUtil.setGrantPermissionDao(grantPermissionDao);
    Set<String> hasPermissionsList = UserServiceUtil.hasGrantPermissions(user, permissionArray);
    // 返回json
    return UserServiceUtil.comparePermissionsResult(hasPermissionsList, permissionArray);
  }

  public void loginSuccess() {}

  public void loginFailure() {}

  // 获取用户角色for Authoritys
  public Set<String> getUserRoleCodesAuthoritys(User user) {
    Set<String> authoritys = new HashSet<>();
    try {
      // 取用户的角色列表
      //            UserServiceUtil.setDepartmentDao(departmentDao);
      //      UserServiceUtil.setGrantPermissionDao(grantPermissionDao);
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

  /**
   * 用户注册
   *
   * @param user 用户对象
   */
  public LoginUser register(User user) {
    if (StringUtil.isBlank(user.getUsername())) {
      user.setUsername(user.getPhone().getNumber());
    }
    user = this.save(user);
    return UserUtil.buildLoginUser(user);
  }

  @Transactional(readOnly = true)
  public List<User> findAll(PropertyFilter filter) {
    return this.userDao.findAll(filter);
  }
}
