package cn.asany.security.core.graphql.resolver;

import cn.asany.security.core.bean.Permission;
import cn.asany.security.core.bean.Role;
import cn.asany.security.core.service.GrantPermissionService;
import cn.asany.security.core.service.RoleService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: guoyong
 * @description: 角色元素填充类
 * @create: 2020/6/18 16:56
 */
@Component
public class RoleGraphQLResolver implements GraphQLResolver<Role> {
  @Autowired private RoleService roleService;
  @Autowired private GrantPermissionService grantPermissionService;

  public List<Permission> permissions(Role role) {
    return grantPermissionService.getPermissionsByRoleId("");
  }

  //    public List<Department> departments(Role role) {
  //        return roleService.getRoleDepartments(role.getId());
  //    }
  //
  //    public List<EmployeeGroup> employeeGroups(Role role) {
  //        return roleService.getRoleEmployeeGroups(role.getId());
  //    }
  //
  //    public List<Position> positions(Role role) {
  //        return roleService.getRolePositions(role.getId());
  //    }

}
