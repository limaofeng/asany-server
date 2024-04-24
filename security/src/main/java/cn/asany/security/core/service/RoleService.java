package cn.asany.security.core.service;

import cn.asany.security.core.dao.PermissionStatementDao;
import cn.asany.security.core.dao.RoleDao;
import cn.asany.security.core.dao.RoleGrantDao;
import cn.asany.security.core.domain.PermissionStatement;
import cn.asany.security.core.domain.Role;
import cn.asany.security.core.domain.enums.GranteeType;
import cn.asany.security.core.domain.enums.RoleType;
import cn.asany.security.core.exception.ValidDataException;
import cn.asany.security.core.graphql.enums.RoleAssignEntityTypeEnum;
import java.util.*;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

  @Autowired private final RoleDao roleDao;

  //  @Autowired private GrantPermissionDao grantPermissionDao;

  @Autowired private RoleGrantDao roleGrantDao;

  @Autowired private PermissionStatementDao permissionStatementDao;

  private static final String CACHE_KEY = "ac::role";

  @Autowired
  public RoleService(RoleDao roleDao) {
    this.roleDao = roleDao;
  }

  public List<Role> getAll() {
    return roleDao.findAll(Example.of(Role.builder().build()));
  }

  public Page<Role> findPage(Pageable pageable, PropertyFilter filter) {
    return this.roleDao.findPage(pageable, filter);
  }

  public Role save(Role role) {
    role.setType(ObjectUtil.defaultValue(role.getType(), RoleType.CLASSIC));
    return roleDao.save(role);
  }

  public Role update(Long id, Role role, boolean merge) {
    if (!roleDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.ROLE_NOTEXISTS, id);
    }
    role.setId(id);
    return this.roleDao.save(role);
  }

  @Cacheable(key = "'id=' + #p0", value = CACHE_KEY)
  public Optional<Role> findById(Long id) {
    return this.roleDao.findById(id);
  }

  public Optional<Role> findByCode(String role) {
    return this.roleDao.findOne(PropertyFilter.newFilter().equal("code", role));
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
      PermissionStatement permissionStatement = permissionStatementDao.getOne(Long.valueOf(eId));
      //            grantPermissionDao.save(GrantPermission.builder()
      //                    .permission(permission)
      //                    .securityType(SecurityType.role)
      //                    .value(roleId).build());
    }
  }

  public List<Role> findAllByUser(Long userId) {
    Set<Long> roleIds =
        this.roleGrantDao
            .findAll(
                PropertyFilter.newFilter()
                    .equal("grantee.type", GranteeType.USER)
                    .equal("grantee.value", String.valueOf(userId)))
            .stream()
            .map(item -> item.getRole().getId())
            .collect(Collectors.toSet());
    RoleService self = SpringBeanUtils.getBean(RoleService.class);
    return roleIds.stream()
        .map(self::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }
}
