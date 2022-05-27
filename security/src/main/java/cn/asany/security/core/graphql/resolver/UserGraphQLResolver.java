package cn.asany.security.core.graphql.resolver;

import cn.asany.base.common.SecurityScope;
import cn.asany.base.common.SecurityType;
import cn.asany.security.core.domain.GrantPermission;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.service.GrantPermissionService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户详情
 *
 * @author limaofeng
 * @version V1.0
 */
@Component
public class UserGraphQLResolver implements GraphQLResolver<User> {

  @Autowired private GrantPermissionService grantPermissionService;
  //    @Autowired
  //    private EmployeeService employeeService;

  public String name(User user) {
    return user.getName();
  }

  public String username(User user) {
    return user.getUsername();
  }

  public String password(User user) {
    return "******";
  }

  //    public Optional<Employee> profile(User user) {
  //        Optional<Employee> employee = Optional.of(new Employee());
  //        return employee;
  //    }

  public List<GrantPermission> permissions(User user, String resourceType, String permissionKey) {
    List<GrantPermission> grantPermissions = new ArrayList<>();
    if (user != null) {
      grantPermissions.addAll(
          grantPermissionService.getPermissions(
              resourceType,
              permissionKey,
              SecurityScope.newInstance(SecurityType.user, user.getId().toString())));
    }
    return grantPermissions;
  }

  public String deptsNodesNames(User user) {
    //        Employee employee = user.getEmployee();
    //        if (employee == null) {
    //            return "";
    //        }
    //        String orgId = user.getOrganization().getId();
    //        return employeeService.getDeptsNodesNames(orgId, employee);
    return null;
  }

  //    public Employee emplyee(User user) {
  //        return user.getEmployee();
  //    }
}
