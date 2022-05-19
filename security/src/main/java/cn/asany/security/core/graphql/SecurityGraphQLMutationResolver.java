package cn.asany.security.core.graphql;

import cn.asany.security.core.bean.*;
import cn.asany.security.core.graphql.input.GrantPermissionByUserInput;
import cn.asany.security.core.graphql.models.*;
import cn.asany.security.core.service.*;
import com.github.stuxuhai.jpinyin.PinyinException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.LimitPageRequest;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-04-01 15:17
 */
@Component
public class SecurityGraphQLMutationResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  @Autowired private RoleService roleService;
  @Autowired private UserService userService;
  @Autowired private RoleScopeService roleScopeService;
  @Autowired private PermissionService permissionService;
  @Autowired private GrantPermissionService grantPermissionService;

  /** 查询所有用户 - 分页 */
  public UserConnection usersConnection(
      UserFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pageable pageable = PageRequest.of(page, pageSize, orderBy.toSort());
    PropertyFilterBuilder builder = ObjectUtil.defaultValue(filter, new UserFilter()).getBuilder();
    return Kit.connection(userService.findPage(pageable, builder.build()), UserConnection.class);
  }

  /** 查询所有用户 - 列表 */
  public List<User> users(
      UserFilter filter, int skip, int after, int before, int first, int last, OrderBy orderBy) {
    Pageable pageable = LimitPageRequest.of(skip, first, orderBy.toSort());
    PropertyFilterBuilder builder = ObjectUtil.defaultValue(filter, new UserFilter()).getBuilder();
    return userService.findPage(pageable, builder.build()).getContent();
  }

  /**
   * 新增角色
   *
   * @param input
   * @return
   */
  public Role createRole(String organization, RoleInput input) throws PinyinException {
    Role role = new Role();
    BeanUtils.copyProperties(input, role);
    //        role.setOrganization(Organization.builder().id(organization).build());
    //    if (StringUtil.isBlank(role.getId())) {
    //      role.setId((organization + "_" + PinyinUtils.getShort(input.getName())).toUpperCase());
    //    }
    //    role.setScope(RoleScope.builder().id(input.getScopes()).build());
    //
    // role.setRoleType(RoleType.builder().id(getRoleTypeInput(input.getRoleTypeInput())).build());
    return roleService.save(role);
  }

  /**
   * 更新角色
   *
   * @param id
   * @param merge
   * @param input
   * @return
   */
  public Role updateRole(Long id, Boolean merge, RoleInput input) {
    Role role = new Role();
    //    BeanUtils.copyProperties(input, role);
    //    role.setScope(RoleScope.builder().id(input.getScopes()).build());
    //
    // role.setRoleType(RoleType.builder().id(getRoleTypeInput(input.getRoleTypeInput())).build());
    return roleService.update(id, merge, role);
  }

  // 处理分类类型
  private String getRoleTypeInput(String roleTypeInput) {
    //    if (StringUtils.isEmpty(roleTypeInput)) {
    //      return RoleType.UNKNOWN;
    //    }
    return roleTypeInput;
  }

  /**
   * 删除角色
   *
   * @param id
   * @return
   */
  public Boolean removeRole(Long id) {
    roleService.delete(id);
    return true;
  }

  /**
   * 新增用户
   *
   * @param organization
   * @param input
   * @return
   */
  public User createUser(String organization, UserInput input) {
    User user = new User();
    BeanUtils.copyProperties(input, user, "grants", "tel", "roles");
    //    if (input.getTel() != null) {
    //      user.set("tel", input.getTel());
    //    } else {
    //      user.set("tel", "");
    //    }
    //        user.setOrganization(Organization.builder().id(organization).build());
    //        if (input.getEmployee() != null) {
    //            user.setEmployee(Employee.builder().id(input.getEmployee()).build());
    //        }
    //    List<Role> roles = new ArrayList<>();
    //    if (input.getRoles() != null) {
    //      for (String roleid : input.getRoles()) {
    //        roles.add(Role.builder().id(roleid).build());
    //      }
    //    }
    // 后加 默认添加用户角色
    //    roles.add(Role.builder().id("USER").build());

    //    user.setRoles(roles);
    //    user.setUserType(UserType.USER);
    user = userService.save(user);
    // 保存权限
    //    if (input.getGrants() != null) {
    //      user.setGrants(
    //          GrantPermissionUtils.allocation(
    //              SecurityType.user,
    //              user.getId().toString(),
    //              getGrantPermissionByUser(input.getGrants())));
    //    }
    return user;
  }

  private List<GrantPermission> getGrantPermissionByUser(
      List<GrantPermissionByUserInput> grantInputs) {
    return grantInputs.stream()
        .map(
            item ->
                GrantPermission.builder()
                    .permission(Permission.builder().id(item.getPermission()).build())
                    //                    .resource(item.getResource())
                    .build())
        .collect(Collectors.toList());
  }

  public Boolean removeUser(Long id) {
    userService.delete(id);
    return true;
  }

  //  public RoleScope updateBusiness(String id, Boolean merge, BusinessScopeInput input) {
  //    RoleScope roleScope = new RoleScope();
  //    BeanUtils.copyProperties(input, roleScope, "code");
  //    roleScope.setId(id);
  //    return roleScopeService.update(id, merge, roleScope);
  //  }

  //  public RoleScope createBusiness(BusinessScopeInput input) {
  //    RoleScope roleScope = new RoleScope();
  //    BeanUtils.copyProperties(input, roleScope);
  //    roleScope.setId(input.getCode());
  //    return roleScopeService.save(roleScope);
  //  }

  //  /**
  //   * 新增角色分类
  //   *
  //   * @param input
  //   * @return
  //   */
  //  public RoleType createRoleType(RoleTypeInput input) throws PinyinException {
  //    RoleType roleType = new RoleType();
  //    BeanUtils.copyProperties(input, roleType);
  //    return roleService.saveRoleType(roleType);
  //  }

  //  /**
  //   * 更新角色分类
  //   *
  //   * @param id
  //   * @param merge
  //   * @param input
  //   * @return
  //   */
  //  public RoleType updateRoleType(String id, Boolean merge, RoleTypeInput input) {
  //    RoleType roleType = new RoleType();
  //    BeanUtils.copyProperties(input, roleType);
  //    return roleService.updateRoleType(id, merge, roleType);
  //  }

  //  /**
  //   * 删除角色分类
  //   *
  //   * @param id
  //   * @return
  //   */
  //  public Boolean removeRoleType(String id) {
  //    roleService.deleteRoleType(id);
  //    return true;
  //  }

  /**
   * 新增权限分类
   *
   * @param input
   * @return
   */
  public PermissionType createPermissionType(PermissionTypeInput input) {
    PermissionType type = new PermissionType();
    BeanUtils.copyProperties(input, type);
    return permissionService.savePermissionType(type);
  }

  /**
   * 更新权限分类
   *
   * @param id
   * @param merge
   * @param input
   * @return
   */
  public PermissionType updatePermissionType(String id, Boolean merge, PermissionTypeInput input) {
    PermissionType permissionCategory = new PermissionType();
    BeanUtils.copyProperties(input, permissionCategory);
    return permissionService.updatePermissionType(id, merge, permissionCategory);
  }

  /**
   * 删除权限分类
   *
   * @param id
   * @return
   */
  public Boolean removePermissionType(String id) {
    permissionService.deletePermissionType(id);
    return true;
  }

  /**
   * 新增权限
   *
   * @param input
   * @return
   */
  public Permission createPermission(PermissionUpdateInput input) {
    Permission permission = new Permission();
    BeanUtils.copyProperties(input, permission);
    permission.setType(PermissionType.builder().id("").build());
    return permissionService.save(permission);
  }

  // 处理分类类型
  private String getPermissionTypeInput(String PermissionTypeInput) {
    if (StringUtils.isEmpty(PermissionTypeInput)) {
      return PermissionType.UNKNOWN;
    }
    return PermissionTypeInput;
  }

  /**
   * 更新权限
   *
   * @param id
   * @param merge
   * @param input
   * @return
   */
  public Permission updatePermission(String id, Boolean merge, PermissionUpdateInput input) {
    Permission permission = new Permission();
    BeanUtils.copyProperties(input, permission);
    permission.setType(PermissionType.builder().id(getPermissionTypeInput("")).build());
    return permissionService.update(id, merge, permission);
  }

  /**
   * 删除权限
   *
   * @param id
   * @return
   */
  public Boolean removePermission(String id) {
    permissionService.delete(id);
    return true;
  }

  /**
   * 权限分配(分配对象：部门、组、岗位、用户)
   *
   * @param permissionIds 角色IDs（格式：可以逗号间隔）
   * @param entityTypeIds 实体Ids组合（格式：type:Ids;;type:Ids）
   * @return
   */
  public Boolean assignPermissions(String permissionIds, String entityTypeIds) {
    grantPermissionService.assignPermissions(permissionIds, entityTypeIds);
    return true;
  }

  /**
   * 角色分配(分配对象：部门、组、岗位、用户)
   *
   * @param roleIds 角色IDs（格式：可以逗号间隔）
   * @param entityTypeIds 实体Ids组合（格式：type:Ids;;type:Ids）
   * @return
   */
  public Boolean assignRoles(String roleIds, String entityTypeIds) {
    roleService.assignRoles(roleIds, entityTypeIds);
    return true;
  }
}
