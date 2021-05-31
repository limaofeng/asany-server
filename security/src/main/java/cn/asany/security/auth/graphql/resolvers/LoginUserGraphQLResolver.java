package cn.asany.security.auth.graphql.resolvers;

import cn.asany.security.core.bean.GrantPermission;
import cn.asany.security.core.bean.enums.IdType;
import cn.asany.security.core.service.GrantPermissionService;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * LoginUser Resolver
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-08 16:54
 */
@Component
public class LoginUserGraphQLResolver implements GraphQLResolver<LoginUser> {

    @Autowired
    private GrantPermissionService grantPermissionService;
//    @Autowired
//    private OrganizationEmployeeService organizationEmployeeService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private MobileButtonService mobileButtonService;

//    private Employee employee(LoginUser user) {
//        if (!StringUtils.isEmpty(user)) {
//            return user.get("employee");
//        }
//        return null;
//    }

    public String uid(LoginUser user, IdType idType) {
//        if (idType == null || idType == IdType.id) {
//            return user.getUid();
//        }
//        if ("employee".equals(user.getType())) {
//            Employee employee = employee(user);
//            if (employee == null) {
//                return null;
//            }
//            return employee.getLinkId(idType);
//        }
        return null;
    }

    public String avatar(LoginUser user) {
        if (user == null) {
            return null;
        }
//        if ("employee".equals(user.getType())) {
//            Employee employee = employee(user);
//            if (employee == null) {
//                return null;
//            }
//            FileObject avatar = employee.getAvatar();
//            if (avatar != null) {
//                return avatar.getPath();
//            }
//            return null;
//        }
        return null;
    }

    public String jobNumber(LoginUser user) {
//        if ("employee".equals(user.getType())) {
//            Employee employee = employee(user);
//            if (employee == null) {
//                return null;
//            }
//            return employee.getJobNumber();
//        }
        return null;
    }

    public Set<String> authoritys(LoginUser loginUser) {
//        User user = loginUser.get("user");
        Set<String> authoritys = new HashSet<>();
//        if (user != null) {
//            authoritys.addAll(user.getAuthoritys());
//        }
//        if ("employee".equals(loginUser.getType())) {
//            Employee employee = employee(loginUser);
//            if (employee == null) {
//                return null;
//            }
//            authoritys.addAll(employee.getAuthoritys());
//        }
//        Set<String> userRoleAuthoritys = userService.getUserRoleCodesAuthoritys(user);
//        authoritys.addAll(userRoleAuthoritys);
        return authoritys;
    }

    public List<GrantPermission> permissions(LoginUser loginUser) {
//        User user = loginUser.get("user");
//        List<GrantPermission> grantPermissions = new ArrayList<>();
//        if (user != null) {
//            grantPermissions.addAll(grantPermissionService.getPermissions(SecurityScope.newInstance(SecurityType.user, user.getId().toString())));
//        }
        return new ArrayList<>();
    }

//    public List<Department> departments(LoginUser loginUser, String organizationId) {
//        Employee employee = employee(loginUser);
//        if (employee == null) {
//            return null;
//        }
//        return organizationEmployeeService.findDepartments(employee.getId(), organizationId)
//            .stream().distinct().collect(Collectors.toList());
//    }

//    public List<Position> positions(LoginUser loginUser, String organizationId, Long departmentId) {
//        Employee employee = employee(loginUser);
//        if (employee == null) {
//            return null;
//        }
//        return organizationEmployeeService.findPostions(employee.getId(), organizationId, departmentId);
//    }

//    public List<Role> roles(LoginUser loginUser) {
//        Employee employee = employee(loginUser);
//        if (employee == null) {
//            return null;
//        }
//        return employee.getUser().getRoles();
//    }

    /**
     * 当前部门
     *
     * @param loginUser
     * @return
     */
//    public Department currentDepartment(LoginUser loginUser, String organization) {
//        OrganizationEmployee organizationEmployee = getOrganizationEmployee(loginUser, organization);
//        if (organizationEmployee != null) {
//            return organizationEmployee.getDepartment();
//        }
//        return null;
//    }

    /**
     * 当前职务
     *
     * @param loginUser
     * @return
     */
//    public Position currentPosition(LoginUser loginUser, String organization) {
//        OrganizationEmployee organizationEmployee = getOrganizationEmployee(loginUser, organization);
//        if (organizationEmployee != null) {
//            return organizationEmployee.getPosition();
//        }
//        return null;
//    }

    /**
     * 获取组织与人员关联对象：OrganizaitonEmployee
     *
     * @param loginUser
     * @return
     */
//    public OrganizationEmployee getOrganizationEmployee(LoginUser loginUser, String organization) {
//        Employee employee = employee(loginUser);
//        if (employee == null) {
//            return null;
//        }
//        return organizationEmployeeService.get(organization, employee.getId()).orElse(null);
//    }

}
