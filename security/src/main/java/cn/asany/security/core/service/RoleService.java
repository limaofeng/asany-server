package cn.asany.security.core.service;

import cn.asany.security.core.bean.Permission;
import cn.asany.security.core.bean.Role;
import cn.asany.security.core.bean.RoleScope;
import cn.asany.security.core.bean.RoleType;
import cn.asany.security.core.dao.*;
import cn.asany.security.core.exception.ValidDataException;
import cn.asany.security.core.graphql.enums.RoleAssignEntityTypeEnum;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {
  @Autowired private final RoleDao roleDao;

  @Autowired private RoleScopeDao roleScopeDao;

  @Autowired private RoleTypeDao roleTypeDao;

  @Autowired private GrantPermissionDao grantPermissionDao;

  @Autowired private PermissionDao permissionDao;

  @Autowired
  public RoleService(RoleDao roleDao) {
    this.roleDao = roleDao;
  }

  public List<Role> getAll() {
    return roleDao.findAll(Example.of(Role.builder().enabled(true).build()));
  }

  public Pager<Role> findPager(Pager<Role> pager, List<PropertyFilter> filters) {
    return this.roleDao.findPager(pager, filters);
  }

  public Role save(Role role) {
    return roleDao.save(role);
  }

  public Role update(String id, boolean merge, Role role) {
    if (!roleDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.ROLE_NOTEXISTS, id);
    }
    role.setId(id);
    return this.roleDao.save(role);
  }

  public Role get(String id) {
    return this.roleDao.getOne(id);
  }

  public void delete(String... ids) {
    // 先删除关联关系
    for (String id : ids) {
      deleteRoleRelation(id);
    }
    this.roleDao.deleteInBatch(
        Arrays.asList(ids).stream()
            .map(id -> Role.builder().id(id).build())
            .collect(Collectors.toList()));
  }

  private void deleteRoleRelation(String roleCode) {
    deleteRoleEntity(roleCode, RoleAssignEntityTypeEnum.department.name());
    deleteRoleEntity(roleCode, RoleAssignEntityTypeEnum.employeeGroup.name());
    deleteRoleEntity(roleCode, RoleAssignEntityTypeEnum.employeePosition.name());
    deleteRoleEntity(roleCode, RoleAssignEntityTypeEnum.user.name());
    deleteRoleEntity(roleCode, RoleAssignEntityTypeEnum.permisstion.name());
  }

  //    public List<Role> getAllByOrg(String org, RoleScope scope) {
  //        Example<Role> example =
  // Example.of(Role.builder().scope(scope).organization(Organization.builder().id(org).build()).build());
  //        return this.roleDao.findAll(example);
  //    }

  public List<Role> getAll(RoleScope scope) {
    Example<Role> example = Example.of(Role.builder().scope(scope).build());
    return this.roleDao.findAll(example);
  }

  public Pager<RoleType> findTypePager(Pager<RoleType> pager, List<PropertyFilter> filters) {
    return this.roleTypeDao.findPager(pager, filters);
  }

  public RoleType saveRoleType(RoleType roleType) {
    if (roleTypeDao.existsById(roleType.getId())) {
      throw new ValidDataException(ValidDataException.ROLETYPE_EXISTS, roleType.getId());
    }
    return roleTypeDao.save(roleType);
  }

  public RoleType updateRoleType(String id, Boolean merge, RoleType roleType) {
    if (!roleTypeDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.ROLETYPE_NOTEXISTS, id);
    }
    roleType.setId(id);
    return roleTypeDao.save(roleType);
  }

  public void deleteRoleType(String id) {
    if (!roleTypeDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.ROLETYPE_NOTEXISTS, id);
    }
    RoleType roleType = roleTypeDao.getOne(id);
    if (CollectionUtils.isEmpty(roleType.getRoles()) || roleType.getRoles().size() == 0) {
      roleTypeDao.delete(roleType);
    } else {
      throw new ValidDataException(ValidDataException.ROLETYPE_HAS_ROLES, id);
    }
  }

  //    public List<Department> getRoleDepartments(String roleId) {
  //        List<RoleOfDepartment> departmentIds =
  // roleOfDepartmentDao.findRoleOfDepartmentPkDepartmentIdByPkRoleCode(roleId);
  //        List<Department> list = new ArrayList<>();
  //        for (RoleOfDepartment rod : departmentIds) {
  //            if (departmentDao.existsById(rod.getPk().getDepartmentId())) {
  //                Department department = departmentDao.getOne(rod.getPk().getDepartmentId());
  //                list.add(department);
  //            }
  //        }
  //        return list;
  //    }

  //    public List<EmployeeGroup> getRoleEmployeeGroups(String roleId) {
  //        List<RoleOfEmplyeeGroup> groupIds =
  // roleOfEmplyeeGroupDao.findRoleOfEmplyeeGroupByPkRoleCode(roleId);
  //        List<EmployeeGroup> list = new ArrayList<>();
  //        for (RoleOfEmplyeeGroup reg : groupIds) {
  //            if (employeeGroupDao.existsById(reg.getPk().getEmployeeGroupId())) {
  //                EmployeeGroup employeeGroup =
  // employeeGroupDao.getOne(reg.getPk().getEmployeeGroupId());
  //                list.add(employeeGroup);
  //            }
  //        }
  //        return list;
  //    }

  //    public List<Position> getRolePositions(String roleId) {
  //        List<RoleOfPosition> positionIds =
  // roleOfPositionDao.findRoleOfPositionPkPositionIdByPkRoleCode(roleId);
  //        List<Position> list = new ArrayList<>();
  //        for (RoleOfPosition rop : positionIds) {
  //            if (positionDao.existsById(rop.getPk().getPositionId())) {
  //                Position position = positionDao.getOne(rop.getPk().getPositionId());
  //                list.add(position);
  //            }
  //        }
  //        return list;
  //    }

  /**
   * 分配角色
   *
   * @param roleIds 角色ids
   * @param entityTypeIds 实体Ids组合（格式：type:Ids;;type:Ids）
   */
  public void assignRoles(String roleIds, String entityTypeIds) {
    for (String roleId : roleIds.split(",")) {
      assignRole(roleId.trim(), entityTypeIds.trim());
    }
  }

  private void assignRole(String roleId, String entityTypeIds) {
    String[] typeIds = entityTypeIds.split(";;");
    for (String values : typeIds) {
      // values格式：type:Ids?
      String[] arr = values.split(":");
      String entityType = arr[0];
      // 先删除
      deleteRoleEntity(roleId, entityType);
      if (arr.length != 2) {
        continue;
      }
      String entityIds = arr[1];
      assignRolesToEntity(roleId, entityType, entityIds);
    }
  }

  private void deleteRoleEntity(String roleCode, String entityType) {
    if (RoleAssignEntityTypeEnum.department.name().equals(entityType)) {
      //            roleOfDepartmentDao.deleteRoleOfDepartmentByPkRoleCode(roleCode);
      //            roleOfDepartmentDao.flush();
    } else if (RoleAssignEntityTypeEnum.employeeGroup.name().equals(entityType)) {
      //            roleOfEmplyeeGroupDao.deleteRoleOfEmplyeeGroupByPkRoleCode(roleCode);
      //            roleOfEmplyeeGroupDao.flush();
    } else if (RoleAssignEntityTypeEnum.employeePosition.name().equals(entityType)) {
      //            roleOfPositionDao.deleteRoleOfPositionByPkRoleCode(roleCode);
      //            roleOfPositionDao.flush();
    } else if (RoleAssignEntityTypeEnum.user.name().equals(entityType)) {
      //            roleOfUserDao.deleteRoleOfUserByPkRoleCode(roleCode);
      //            roleOfUserDao.flush();
    } else if (RoleAssignEntityTypeEnum.permisstion.name().equals(entityType)) {
      //            grantPermissionDao.deleteGrantPermissionsBySecurityTypeAndValue(
      //                    SecurityType.role, roleCode
      //            );
      grantPermissionDao.flush();
    }
  }

  private void assignRolesToEntity(String roleId, String entityType, String entityIds) {
    if (RoleAssignEntityTypeEnum.department.name().equals(entityType)) {
      roleAssignToDepartment(roleId, entityIds);
    } else if (RoleAssignEntityTypeEnum.employeeGroup.name().equals(entityType)) {
      roleAssignToEmployeeGroup(roleId, entityIds);
    } else if (RoleAssignEntityTypeEnum.employeePosition.name().equals(entityType)) {
      roleAssignToEmployeePositions(roleId, entityIds);
    } else if (RoleAssignEntityTypeEnum.user.name().equals(entityType)) {
      roleAssignToUsers(roleId, entityIds);
    } else if (RoleAssignEntityTypeEnum.permisstion.name().equals(entityType)) {
      roleAssignPermisstions(roleId, entityIds);
    }
  }

  /** 角色分配到部门 */
  public void roleAssignToDepartment(String roleId, String departmentIds) {
    String[] entIds = departmentIds.split(",");
    for (String eId : entIds) {
      //            roleOfDepartmentDao.save(RoleOfDepartment.builder()
      //                    .pk(RoleOfDepartment.RoleDepartmentKey.builder()
      //                            .roleCode(roleId)
      //                            .departmentId(StringUtils.stringToInteger(eId)).build())
      //                    .build());
    }
  }

  /** 角色分配到组 */
  public void roleAssignToEmployeeGroup(String roleId, String employeeGroupIds) {
    String[] entIds = employeeGroupIds.split(",");
    for (String eId : entIds) {
      //            roleOfEmplyeeGroupDao.save(RoleOfEmplyeeGroup.builder()
      //                    .pk(RoleOfEmplyeeGroup.RoleGroupKey.builder()
      //                            .roleCode(roleId)
      //                            .employeeGroupId(StringUtils.stringToInteger(eId)).build())
      //                    .build());
    }
  }

  /** 角色分配到岗位 */
  public void roleAssignToEmployeePositions(String roleId, String employeePositionIds) {
    String[] entIds = employeePositionIds.split(",");
    for (String eId : entIds) {
      //            roleOfPositionDao.save(RoleOfPosition.builder()
      //                    .pk(RoleOfPosition.RolePositionKey.builder()
      //                            .roleCode(roleId)
      //                            .positionId(StringUtils.stringToInteger(eId)).build())
      //                    .build());
    }
  }

  /** 角色分配到用户 */
  public void roleAssignToUsers(String roleId, String userIds) {
    String[] entIds = userIds.split(",");
    for (String eId : entIds) {
      //            roleOfUserDao.save(RoleOfUser.builder()
      //                    .pk(RoleOfUser.RoleUserKey.builder()
      //                            .roleCode(roleId)
      //                            .userId(StringUtils.stringToInteger(eId)).build())
      //                    .build());
    }
  }

  /**
   * 角色分配权限
   *
   * @param roleId 角色id
   * @param permisstionIds 权限ids
   */
  private void roleAssignPermisstions(String roleId, String permisstionIds) {
    String[] entIds = permisstionIds.split(",");
    for (String eId : entIds) {
      Permission permission = permissionDao.getOne(eId);
      //            grantPermissionDao.save(GrantPermission.builder()
      //                    .permission(permission)
      //                    .securityType(SecurityType.role)
      //                    .value(roleId).build());
    }
  }

  public List<Role> findRoleData(List<PropertyFilter> filters) {
    Pager<Role> objectPager = new Pager<>();
    objectPager.setPageSize(10000);
    Pager<Role> pager = roleDao.findPager(objectPager, filters);
    List<Role> pageItems = pager.getPageItems();
    return pageItems;
  }
}
