package cn.asany.security.core.graphql.resolver;

import cn.asany.security.core.domain.PermissionStatement;
import cn.asany.security.core.graphql.types.SecurityScopeObject;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 权限 GraphQLResolver
 *
 * @author limaofeng
 */
@Component
public class PermissionGraphQLResolver implements GraphQLResolver<PermissionStatement> {
  //    @Autowired
  //    private SecurityService securityService;
  //  @Autowired private GrantPermissionService grantPermissionService;

  public List<SecurityScopeObject> grants(PermissionStatement permissionStatement) {
    //    if (ObjectUtils.isEmpty(permission.getGrants())) {
    return new ArrayList<SecurityScopeObject>();
    //    }
    //        return securityService.getSecurityScopeObjects(null,
    // false,permission.getGrants().stream().map(item ->
    // SecurityScope.newInstance(item.getSecurityType(),
    // item.getValue())).collect(Collectors.toList()), null);
  }

  //  public List<GrantPermission> grantPermissions(PermissionStatement permissionStatement) {
  //    //    List<GrantPermission> list =
  //    //        grantPermissionService.getGrantPermissionsByPermissionId(permission.getId());
  //    return null;
  //  }
}
