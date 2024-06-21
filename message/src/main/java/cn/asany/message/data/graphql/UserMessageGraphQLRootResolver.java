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
package cn.asany.message.data.graphql;

import cn.asany.message.data.graphql.input.UserMessageWhereInput;
import cn.asany.message.data.graphql.type.UserMessageConnection;
import cn.asany.message.data.service.UserMessageService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 用户消息 GraphQL Root Resolver
 *
 * @author limaofeng
 */
@Component
public class UserMessageGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final UserMessageService userMessageService;

  public UserMessageGraphQLRootResolver(UserMessageService userMessageService) {
    this.userMessageService = userMessageService;
  }

  public UserMessageConnection userMessages(
      UserMessageWhereInput where, int first, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        userMessageService.findPage(pageable, where.toFilter()), UserMessageConnection.class);
  }
}
