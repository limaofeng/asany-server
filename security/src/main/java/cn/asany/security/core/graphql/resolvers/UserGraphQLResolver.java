package cn.asany.security.core.graphql.resolvers;

import cn.asany.base.common.SecurityScope;
import cn.asany.base.common.SecurityType;
import cn.asany.security.core.bean.GrantPermission;
import cn.asany.security.core.bean.User;
import cn.asany.security.core.service.GrantPermissionService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-04-02 12:01
 */
@Component
public class UserGraphQLResolver implements GraphQLResolver<User> {

    @Autowired
    private GrantPermissionService grantPermissionService;
//    @Autowired
//    private EmployeeService employeeService;

    public String tel(User user) {
        return user.get("tel");
    }

    public String name(User user) {
        return user.getUsername();
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
            grantPermissions.addAll(grantPermissionService.getPermissions(resourceType, permissionKey, SecurityScope.newInstance(SecurityType.user, user.getId().toString())));
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
