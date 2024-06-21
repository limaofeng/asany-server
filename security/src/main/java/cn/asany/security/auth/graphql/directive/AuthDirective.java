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
package cn.asany.security.auth.graphql.directive;

import cn.asany.security.auth.service.AuthInfoService;
import cn.asany.security.core.domain.enums.AccessLevel;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 权限信息
 *
 * @author limaofeng
 */
public class AuthDirective implements SchemaDirectiveWiring {

  private final AuthInfoService authInfoService;

  public AuthDirective(AuthInfoService authInfoService) {
    this.authInfoService = authInfoService;
  }

  @Override
  public GraphQLFieldDefinition onField(
      SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
    GraphQLFieldDefinition field = env.getElement();
    GraphQLFieldsContainer parentType = env.getFieldsContainer();
    GraphQLDirective directive = env.getDirective();

    String name = DirectiveUtils.getArgumentValue(directive, "name");
    AccessLevel accessLevel =
        AccessLevel.valueOf(DirectiveUtils.getArgumentValue(directive, "accessLevel"));
    String description =
        DirectiveUtils.getArgumentValue(directive, "description", field.getDescription());
    List<String> resourceTypes =
        DirectiveUtils.getArgumentValue(directive, "resourceTypes", Collections.emptyList());
    String endpoint = parentType.getName() + "." + field.getName();

    AuthInfo authInfo =
        authInfoService.save(
            name, description, endpoint, accessLevel, new HashSet<>(resourceTypes));

    //    env.getCodeRegistry()
    //        .dataFetcher(parentType, field, new FieldPermissionDataFetcher(env, authInfo));
    return field;
  }
}
