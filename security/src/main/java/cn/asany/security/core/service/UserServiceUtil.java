package cn.asany.security.core.service;

import cn.asany.security.core.domain.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.util.CollectionUtils;

/**
 * @author: guoyong
 * @description: 处理一些用户逻辑
 * @create: 2020/6/16 15:22
 */
@Deprecated
public class UserServiceUtil {

  //    private static DepartmentDao departmentDao;
  //  private static GrantPermissionDao grantPermissionDao;

  //    public static void setDepartmentDao(DepartmentDao departmentDao) {
  //        UserServiceUtil.departmentDao = departmentDao;
  //    }

  //  public static void setGrantPermissionDao(GrantPermissionDao grantPermissionDao) {
  //    UserServiceUtil.grantPermissionDao = grantPermissionDao;
  //  }

  private UserServiceUtil() {
    throw new IllegalStateException("UserServiceUtil class");
  }

  public static Set<String> hasGrantPermissions(User user, String[] permisstionArray) {
    Set<String> permissions = new HashSet<>();
    // 先判断用户的角色里，有没有授权
    Set<String> userRoles = getUserRoleCodes(user);
    Set<String> rolePermissions = checkRolePermissions(userRoles, permisstionArray);
    permissions.addAll(rolePermissions);
    // 判断机构有没有授权
    Set<String> orgPermissions = checkOrgPermissions(user, permisstionArray);
    permissions.addAll(orgPermissions);
    //        Employee employee = user.getEmployee();
    //        if (employee != null) {
    //            //检查部门有没有授权
    //            Set<String> deptPermissions = checkDeptPermissions(employee, permisstionArray);
    //            permissions.addAll(deptPermissions);
    //            //检查职务有没有授权
    //            Set<String> jobPermissions = checkJobPermissions(employee, permisstionArray);
    //            permissions.addAll(jobPermissions);
    //            //检查岗位有没有授权
    //            Set<String> positionPermissions = checkPositionPermissions(employee,
    // permisstionArray);
    //            permissions.addAll(positionPermissions);
    //            //检查员工有没有授权
    //            Set<String> employeePermissions = checkEmployeePermissions(employee,
    // permisstionArray);
    //            permissions.addAll(employeePermissions);
    //            //检查群组有没有授权
    //            Set<String> groupPermissions = checkGroupPermissions(employee, permisstionArray);
    //            permissions.addAll(groupPermissions);
    //        }
    // 检查用户有没有权限
    Set<String> userPermissions = checkUserPermissions(user, permisstionArray);
    permissions.addAll(userPermissions);
    return permissions;
  }

  // 判断用户的机构是否有权限
  private static Set<String> checkOrgPermissions(User user, String[] permisstionArray) {
    Set<String> hasPermissionsList = new HashSet<>();
    //        if (user.getOrganization() != null) {
    //            for (String permission : permisstionArray) {
    //                int c =
    // grantPermissionDao.countGrantPermissionByPermissionIdAndSecurityTypeAndValue(permission,
    // SecurityType.organization,
    //                        user.getOrganization().getId());
    //                if (c > 0) {
    //                    hasPermissionsList.add(permission);
    //                }
    //            }
    //        }
    return hasPermissionsList;
  }

  // 判断部门是否有权限
  //    private static Set<String> checkDeptPermissions(Employee employee, String[]
  // permisstionArray) {
  //        Set<String> hasPermissionsList = new HashSet<>();
  //        List<String> deptIds = parseLongToStringList(employee.getDepartmentIds());
  //        for (String deptId : deptIds) {
  //            for (String permission : permisstionArray) {
  //                int c =
  // grantPermissionDao.countGrantPermissionByPermissionIdAndSecurityTypeAndValue(permission,
  // SecurityType.department, deptId);
  //                if (c > 0) {
  //                    hasPermissionsList.add(permission);
  //                }
  //            }
  //        }
  //        return hasPermissionsList;
  //    }

  // 判断职务是否有权限
  //    private static Set<String> checkJobPermissions(Employee employee, String[] permisstionArray)
  // {
  //        Set<String> hasPermissionsList = new HashSet<>();
  //        List<String> jobIds = parseJobsToStringList(employee.getEmployeePositions());
  //        for (String jobId : jobIds) {
  //            for (String permission : permisstionArray) {
  //                int c =
  // grantPermissionDao.countGrantPermissionByPermissionIdAndSecurityTypeAndValue(permission,
  // SecurityType.job, jobId);
  //                if (c > 0) {
  //                    hasPermissionsList.add(permission);
  //                }
  //            }
  //        }
  //        return hasPermissionsList;
  //    }

  // 判断岗位是否有权限
  //    private static Set<String> checkPositionPermissions(Employee employee, String[]
  // permisstionArray) {
  //        Set<String> hasPermissionsList = new HashSet<>();
  //        List<String> positionIds = parsePositionsToStringList(employee.getEmployeePositions());
  //        for (String positionId : positionIds) {
  //            for (String permission : permisstionArray) {
  //                int c =
  // grantPermissionDao.countGrantPermissionByPermissionIdAndSecurityTypeAndValue(permission,
  // SecurityType.position, positionId);
  //                if (c > 0) {
  //                    hasPermissionsList.add(permission);
  //                }
  //            }
  //        }
  //        return hasPermissionsList;
  //    }

  // 判断员工是否有权限
  //    private static Set<String> checkEmployeePermissions(Employee employee, String[]
  // permisstionArray) {
  //        Set<String> hasPermissionsList = new HashSet<>();
  //        for (String permission : permisstionArray) {
  //            int c =
  // grantPermissionDao.countGrantPermissionByPermissionIdAndSecurityTypeAndValue(permission,
  // SecurityType.employee,
  //                    String.valueOf(employee.getId()));
  //            if (c > 0) {
  //                hasPermissionsList.add(permission);
  //            }
  //        }
  //        return hasPermissionsList;
  //    }

  // 判断群组是否有权限
  //    private static Set<String> checkGroupPermissions(Employee employee, String[]
  // permisstionArray) {
  //        Set<String> hasPermissionsList = new HashSet<>();
  //        List<String> groupIds = parseGroupsToStringList(employee.getGroups());
  //        for (String groupId : groupIds) {
  //            for (String permission : permisstionArray) {
  //                int c =
  // grantPermissionDao.countGrantPermissionByPermissionIdAndSecurityTypeAndValue(permission,
  // SecurityType.employeeGroup, groupId);
  //                if (c > 0) {
  //                    hasPermissionsList.add(permission);
  //                }
  //            }
  //        }
  //        return hasPermissionsList;
  //    }

  // 判断用户是否有权限
  private static Set<String> checkUserPermissions(User user, String[] permisstionArray) {
    Set<String> hasPermissionsList = new HashSet<>();
    for (String permission : permisstionArray) {
      int c = 0;
      //          grantPermissionDao.countGrantPermissionByPermissionIdAndSecurityTypeAndValue(
      //              permission, SecurityType.user, String.valueOf(user.getId()));
      if (c > 0) {
        hasPermissionsList.add(permission);
      }
    }
    return hasPermissionsList;
  }

  private static Set<String> checkRolePermissions(
      Set<String> userRoles, String[] permisstionArray) {
    Set<String> hasPermissionsList = new HashSet<>();
    for (String roleCode : userRoles) {
      for (String permission : permisstionArray) {
        int c = 0;
        //            grantPermissionDao.countGrantPermissionByPermissionIdAndSecurityTypeAndValue(
        //                permission, SecurityType.role, roleCode);
        if (c > 0) {
          hasPermissionsList.add(permission);
        }
      }
    }
    return hasPermissionsList;
  }

  // 返回用户的角色ID列表
  public static Set<String> getUserRoleCodes(User user) {
    Set<String> roleCodes = new HashSet<>();
    // 用户自己的角色
    Set<String> selfRoleCodes = getUserSelfRoleCodes(user);
    roleCodes.addAll(selfRoleCodes);
    //        Employee employee = user.getEmployee();
    //        if (employee == null) {
    //            return roleCodes;
    //        }
    //        //用户对应员工的部门角色
    //        Set<String> deptRoleCodes = getEmplyeeDeptRoleCodes(employee);
    //        roleCodes.addAll(deptRoleCodes);
    //        //用户组对应的角色
    //        Set<String> groupRoleCodes = getEmplyeeGroupRoleCodes(employee);
    //        roleCodes.addAll(groupRoleCodes);
    //        //员工对应的岗位的角色
    //        Set<String> positionRoleCodes = getEmplyeePositionRoleCodes(employee);
    //        roleCodes.addAll(positionRoleCodes);
    return roleCodes;
  }

  // TODO: 用户自己的角色
  private static Set<String> getUserSelfRoleCodes(User user) {
    Set<String> roleCodes = new HashSet<>();
    //    List<Role> roles = user.getRoles();
    //    if (roles != null) {
    //      for (Role role : roles) {
    //        roleCodes.add(role.getName());
    //      }
    //    }
    return roleCodes;
  }

  // 用户部门的角色
  //    private static Set<String> getEmplyeeDeptRoleCodes(Employee employee) {
  //        Set<String> roleCodes = new HashSet<>();
  //        List<Long> deptIds = employee.getDepartmentIds();
  //        if (deptIds != null) {
  //            for (Long deptId : deptIds) {
  //                if (!departmentDao.existsById(deptId)) {
  //                    continue;
  //                }
  //                Department department = departmentDao.getOne(deptId);
  //                List<Role> roles = department.getRoles();
  //                if (roles != null) {
  //                    for (Role role : roles) {
  //                        roleCodes.add(role.getId());
  //                    }
  //                }
  //            }
  //        }
  //        return roleCodes;
  //    }

  // 员工岗位的角色
  //    private static Set<String> getEmplyeePositionRoleCodes(Employee employee) {
  //        Set<String> roleCodes = new HashSet<>();
  //        List<EmployeePosition> employeePositions = employee.getEmployeePositions();
  //        if (employeePositions != null) {
  //            for (EmployeePosition position : employeePositions) {
  //                if (position.getPosition() != null) {
  //                    List<Role> roles = position.getPosition().getRoles();
  //                    if (roles != null) {
  //                        for (Role role : roles) {
  //                            roleCodes.add(role.getId());
  //                        }
  //                    }
  //                }
  //            }
  //        }
  //        return roleCodes;
  //    }

  // 群组的角色
  //    private static Set<String> getEmplyeeGroupRoleCodes(Employee employee) {
  //        Set<String> roleCodes = new HashSet<>();
  //        List<EmployeeGroup> groups = employee.getGroups();
  //        if (groups != null) {
  //            for (EmployeeGroup group : groups) {
  //                List<Role> roles = group.getRoles();
  //                if (roles != null) {
  //                    for (Role role : roles) {
  //                        roleCodes.add(role.getId());
  //                    }
  //                }
  //            }
  //        }
  //        return roleCodes;
  //    }

  private static List<String> parseLongToStringList(List<Long> list) {
    List<String> data = new ArrayList<>();
    if (CollectionUtils.isEmpty(list)) {
      return data;
    }
    for (Long x : list) {
      data.add(String.valueOf(x));
    }
    return data;
  }

  //    private static List<String> parseJobsToStringList(List<EmployeePosition> list) {
  //        List<String> data = new ArrayList<>();
  //        if (CollectionUtils.isEmpty(list)) {
  //            return data;
  //        }
  //        for (EmployeePosition e : list) {
  //            if (e.getPosition() != null && e.getPosition().getJob() != null) {
  //                data.add(String.valueOf(e.getPosition().getJob().getId()));
  //            }
  //        }
  //        return data;
  //    }

  //    private static List<String> parsePositionsToStringList(List<EmployeePosition> list) {
  //        List<String> data = new ArrayList<>();
  //        if (CollectionUtils.isEmpty(list)) {
  //            return data;
  //        }
  //        for (EmployeePosition e : list) {
  //            if(e.getPosition() != null) {
  //                data.add(String.valueOf(e.getPosition().getId()));
  //            }
  //        }
  //        return data;
  //    }

  //    private static List<String> parseGroupsToStringList(List<EmployeeGroup> list) {
  //        List<String> data = new ArrayList<>();
  //        if (CollectionUtils.isEmpty(list)) {
  //            return data;
  //        }
  //        for (EmployeeGroup e : list) {
  //            data.add(String.valueOf(e.getId()));
  //        }
  //        return data;
  //    }

  public static String comparePermissionsResult(
      Set<String> hasPermissionsList, String[] permisstionArray) {
    // 封装成json返回
    StringBuilder sb = new StringBuilder("{");
    int index = 0;
    for (String permission : permisstionArray) {
      sb.append(index++ == 0 ? "" : ",");
      sb.append(permission).append(":").append(hasPermissionsList.contains(permission));
    }
    sb.append("}");
    return sb.toString();
  }
}
