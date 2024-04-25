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

import cn.asany.security.core.domain.Role;
import cn.asany.security.core.domain.RoleScope;
import cn.asany.security.core.service.RoleService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 角色使用范围
 *
 * @author limaofeng
 * @version V1.0
 */
@Component
public class RoleScopeGraphQLResolver implements GraphQLResolver<RoleScope> {
  private final RoleService roleService;

  public RoleScopeGraphQLResolver(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * 查询所有角色
   *
   * @param roleScope
   * @param organization
   * @return
   */
  public List<Role> roles(RoleScope roleScope, String organization) {
    if (StringUtil.isBlank(organization)) {
      //      roleService.getAll(roleScope);
    }
    return new ArrayList<>(); // roleService.getAllByOrg(organization,roleScope);
  }
}
