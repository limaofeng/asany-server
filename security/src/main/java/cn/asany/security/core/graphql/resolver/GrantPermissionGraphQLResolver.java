package cn.asany.security.core.graphql.resolver;

import cn.asany.base.common.SecurityType;
import cn.asany.security.core.domain.GrantPermission;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.graphql.enums.GrantPermissionReturnObjectType;
import cn.asany.security.core.service.UserService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class GrantPermissionGraphQLResolver implements GraphQLResolver<GrantPermission> {

  //    @Autowired
  //    private DepartmentService departmentService;
  //    @Autowired
  //    private EmployeeService employeeService;
  @Autowired private UserService userService;

  public String resourceType(GrantPermission grantPermission) {
    return ""; // grantPermission.getPermission().getResourceType();
  }

  private Long getObjectId(GrantPermission grantPermission, SecurityType type) {
    //    if (type != grantPermission.getSecurityType()) {
    //      return null;
    //    }
    //    if (StringUtil.isBlank(grantPermission.getValue())) {
    //      return null;
    //    }
    //    return Long.valueOf(grantPermission.getValue());
    return null;
  }

  private Long getObjectId(GrantPermission grantPermission, String type) {
    //    String resourceType = grantPermission.getPermission().getResourceType();
    //    if (!type.equals(resourceType)) {
    //      return null;
    //    }
    //    if (StringUtil.isBlank(grantPermission.getResource())) {
    //      return null;
    //    }
    //    return Long.valueOf(grantPermission.getResource());
    return null;
  }

  private Long getObjectId(
      GrantPermission grantPermission,
      GrantPermissionReturnObjectType type,
      SecurityType securityType) {
    Long objectId = null;
    if (type == GrantPermissionReturnObjectType.resource) {
      objectId = getObjectId(grantPermission, securityType.name());
    } else if (type == GrantPermissionReturnObjectType.security) {
      objectId = getObjectId(grantPermission, securityType);
    }
    return objectId;
  }

  //    public Department department(GrantPermission grantPermission,
  // GrantPermissionReturnObjectType type) {
  //        Long objectId = getObjectId(grantPermission, type, SecurityType.department);
  //        if (objectId == null) {
  //            return null;
  //        }
  //        return this.departmentService.get(objectId);
  //    }

  //    public Employee employee(GrantPermission grantPermission, GrantPermissionReturnObjectType
  // type) {
  //        Long objectId = getObjectId(grantPermission, type, SecurityType.employee);
  //        if (objectId == null) {
  //            return null;
  //        }
  //        return this.employeeService.get(objectId);
  //    }

  public User user(GrantPermission grantPermission, GrantPermissionReturnObjectType type) {
    Long objectId = getObjectId(grantPermission, type, SecurityType.user);
    if (objectId == null) {
      return null;
    }
    return this.userService.get(objectId).orElse(null);
  }
}
