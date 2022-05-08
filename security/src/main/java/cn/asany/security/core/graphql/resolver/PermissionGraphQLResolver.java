package cn.asany.security.core.graphql.resolver;

import cn.asany.security.core.bean.GrantPermission;
import cn.asany.security.core.bean.Permission;
import cn.asany.security.core.graphql.types.SecurityScopeObject;
import cn.asany.security.core.service.GrantPermissionService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-07-17 17:30
 */
@Component
public class PermissionGraphQLResolver implements GraphQLResolver<Permission> {

  //    @Autowired
  //    private SecurityService securityService;
  @Autowired private GrantPermissionService grantPermissionService;

  public List<SecurityScopeObject> grants(Permission permission) {
    if (ObjectUtils.isEmpty(permission.getGrants())) {
      return new ArrayList<SecurityScopeObject>();
    }
    //        return securityService.getSecurityScopeObjects(null,
    // false,permission.getGrants().stream().map(item ->
    // SecurityScope.newInstance(item.getSecurityType(),
    // item.getValue())).collect(Collectors.toList()), null);
    return null;
  }

  public List<GrantPermission> grantPermissions(Permission permission) {
    List<GrantPermission> list =
        grantPermissionService.getGrantPermissionsByPermissionId(permission.getId());
    return list;
  }
}
