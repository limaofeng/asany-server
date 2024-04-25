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

import cn.asany.security.core.graphql.input.PermissionPolicyWhereInput;
import cn.asany.security.core.graphql.types.PermissionPolicyConnection;
import cn.asany.security.core.service.PermissionPolicyService;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 权限策略
 *
 * @author limaofeng
 */
public class PermissionPolicyGraphQLRootResolver {

  private final PermissionPolicyService permissionPolicyService;

  public PermissionPolicyGraphQLRootResolver(PermissionPolicyService permissionPolicyService) {
    this.permissionPolicyService = permissionPolicyService;
  }

  public PermissionPolicyConnection policies(
      PermissionPolicyWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        permissionPolicyService.findPage(pageable, where.toFilter()),
        PermissionPolicyConnection.class);
  }
}
