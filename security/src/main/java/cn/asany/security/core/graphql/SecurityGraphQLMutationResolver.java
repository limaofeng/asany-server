package cn.asany.security.core.graphql;

import cn.asany.security.core.domain.*;
import cn.asany.security.core.graphql.input.*;
import cn.asany.security.core.graphql.types.UserConnection;
import cn.asany.security.core.service.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.LimitPageRequest;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
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
      UserWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    PropertyFilter filter = ObjectUtil.defaultValue(where, new UserWhereInput()).toFilter();
    return Kit.connection(userService.findPage(pageable, filter), UserConnection.class);
  }

  /** 查询所有用户 - 列表 */
  public List<User> users(
      UserWhereInput where, int skip, int after, int before, int first, int last, Sort orderBy) {
    Pageable pageable = LimitPageRequest.of(skip, first, orderBy);
    PropertyFilter filter = ObjectUtil.defaultValue(where, new UserWhereInput()).toFilter();
    return userService.findPage(pageable, filter).getContent();
  }

  private List<GrantPermission> getGrantPermissionByUser(
      List<GrantPermissionByUserInput> grantInputs) {
    return grantInputs.stream()
        .map(
            item ->
                GrantPermission.builder()
                    //
                    // .permission(Permission.builder().id(item.getPermission()).build())
                    //                    .resource(item.getResource())
                    .build())
        .collect(Collectors.toList());
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

  //  /**
  //   * 新增权限分类
  //   *
  //   * @param input
  //   * @return
  //   */
  //  public PermissionType createPermissionType(PermissionTypeInput input) {
  //    PermissionType type = new PermissionType();
  //    BeanUtils.copyProperties(input, type);
  //    return permissionService.savePermissionType(type);
  //  }

  /**
   * 更新权限分类
   *
   * @param id
   * @param merge
   * @param input
   * @return
   */
  //  public PermissionType updatePermissionType(String id, Boolean merge, PermissionTypeInput
  // input) {
  //    PermissionType permissionCategory = new PermissionType();
  //    BeanUtils.copyProperties(input, permissionCategory);
  //    return permissionService.updatePermissionType(id, merge, permissionCategory);
  //  }

  /**
   * 删除权限分类
   *
   * @param id
   * @return
   */
  public Boolean removePermissionType(String id) {
    //    permissionService.deletePermissionType(id);
    return true;
  }

  /**
   * 新增权限
   *
   * @param input
   * @return
   */
  public PermissionStatement createPermission(PermissionStatementUpdateInput input) {
    PermissionStatement permissionStatement = new PermissionStatement();
    BeanUtils.copyProperties(input, permissionStatement);
    //    permission.setType(PermissionType.builder().id("").build());
    //    return permissionService.save(permission);
    return null;
  }

  // 处理分类类型
  private String getPermissionTypeInput(String PermissionTypeInput) {
    if (StringUtils.isEmpty(PermissionTypeInput)) {
      //      return PermissionType.UNKNOWN;
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
  public PermissionStatement updatePermission(
      String id, Boolean merge, PermissionStatementUpdateInput input) {
    PermissionStatement permissionStatement = new PermissionStatement();
    BeanUtils.copyProperties(input, permissionStatement);
    //    permission.setType(PermissionType.builder().id(getPermissionTypeInput("")).build());
    //    return permissionService.update(id, merge, permission);
    return null;
  }

  /**
   * 删除权限
   *
   * @param id
   * @return
   */
  public Boolean removePermission(String id) {
    //    permissionService.delete(id);
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
