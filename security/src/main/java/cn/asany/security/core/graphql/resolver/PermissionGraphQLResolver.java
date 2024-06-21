/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    return new ArrayList<>();
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
