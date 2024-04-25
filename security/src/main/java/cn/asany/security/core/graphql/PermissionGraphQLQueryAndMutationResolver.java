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
package cn.asany.security.core.graphql;

import cn.asany.security.core.domain.Permission;
import cn.asany.security.core.graphql.input.PermissionWhereInput;
import cn.asany.security.core.graphql.types.PermissionConnection;
import cn.asany.security.core.service.PermissionService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PermissionGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final PermissionService permissionService;

  public PermissionGraphQLQueryAndMutationResolver(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  /** 查询权限 */
  public PermissionConnection permissionsConnection(
      PermissionWhereInput where, int page, int pageSize, Sort orderBy) {
    where = ObjectUtil.defaultValue(where, new PermissionWhereInput());
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        permissionService.findPage(pageable, where.toFilter()), PermissionConnection.class);
  }

  /** 查询权限 */
  public List<Permission> permissions(PermissionWhereInput where, Sort orderBy) {
    return permissionService.findAll(where.toFilter(), orderBy);
  }
}
