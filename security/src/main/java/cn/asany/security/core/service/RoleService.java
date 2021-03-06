package cn.asany.security.core.service;

import cn.asany.security.core.dao.GrantPermissionDao;
import cn.asany.security.core.dao.PermissionDao;
import cn.asany.security.core.dao.RoleDao;
import cn.asany.security.core.domain.Permission;
import cn.asany.security.core.domain.Role;
import cn.asany.security.core.domain.enums.RoleType;
import cn.asany.security.core.exception.ValidDataException;
import cn.asany.security.core.graphql.enums.RoleAssignEntityTypeEnum;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {
  @Autowired private final RoleDao roleDao;

  @Autowired private GrantPermissionDao grantPermissionDao;

  @Autowired private PermissionDao permissionDao;

  @Autowired
  public RoleService(RoleDao roleDao) {
    this.roleDao = roleDao;
  }

  public List<Role> getAll() {
    return roleDao.findAll(Example.of(Role.builder().enabled(true).build()));
  }

  public Page<Role> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return this.roleDao.findPage(pageable, filters);
  }

  public Role save(Role role) {
    role.setEnabled(ObjectUtil.defaultValue(role.getEnabled(), true));
    role.setType(ObjectUtil.defaultValue(role.getType(), RoleType.CLASSIC));
    return roleDao.save(role);
  }

  public Role update(Long id, boolean merge, Role role) {
    if (!roleDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.ROLE_NOTEXISTS, id);
    }
    role.setId(id);
    return this.roleDao.save(role);
  }

  public Optional<Role> findById(Long id) {
    return this.roleDao.findById(id);
  }

  public Optional<Role> findByCode(String role) {
    return this.roleDao.findOne(PropertyFilter.builder().equal("code", role).build());
  }

  public void delete(Long... ids) {
    this.delete(Arrays.stream(ids).collect(Collectors.toSet()));
  }

  public void delete(Set<Long> ids) {
    this.roleDao.deleteAllByIdInBatch(ids);
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
   * ????????????
   *
   * @param roleIds ??????ids
   * @param entityTypeIds ??????Ids??????????????????type:Ids;;type:Ids???
   */
  public void assignRoles(String roleIds, String entityTypeIds) {
    for (String roleId : roleIds.split(",")) {
      assignRole(roleId.trim(), entityTypeIds.trim());
    }
  }

  private void assignRole(String roleId, String entityTypeIds) {
    String[] typeIds = entityTypeIds.split(";;");
    for (String values : typeIds) {
      // values?????????type:Ids?
      String[] arr = values.split(":");
      String entityType = arr[0];
      // ?????????
      //      deleteRoleEntity(roleId, entityType);
      if (arr.length != 2) {
        continue;
      }
      String entityIds = arr[1];
      assignRolesToEntity(roleId, entityType, entityIds);
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

  /** ????????????????????? */
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

  /** ?????????????????? */
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

  /** ????????????????????? */
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

  /** ????????????????????? */
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
   * ??????????????????
   *
   * @param roleId ??????id
   * @param permisstionIds ??????ids
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
}
