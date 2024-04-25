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
package cn.asany.pm.issue.priority.graphql;

import cn.asany.pm.issue.priority.graphql.connection.IssuePriorityConnection;
import cn.asany.pm.issue.priority.graphql.filter.IssuePriorityWhereInput;
import cn.asany.pm.issue.priority.service.IssuePriorityService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class PriorityGraphQLQueryResolver implements GraphQLQueryResolver {
  private final IssuePriorityService issuePriorityService;

  public PriorityGraphQLQueryResolver(IssuePriorityService issuePriorityService) {
    this.issuePriorityService = issuePriorityService;
  }

  /** 查询所有优先级 */
  public IssuePriorityConnection issuePrioritys(
      IssuePriorityWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        issuePriorityService.findPage(pageable, where.toFilter()), IssuePriorityConnection.class);
  }
}
