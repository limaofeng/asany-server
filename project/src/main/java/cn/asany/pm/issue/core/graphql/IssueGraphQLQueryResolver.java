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
package cn.asany.pm.issue.core.graphql;

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.issue.core.graphql.input.IssueWhereInput;
import cn.asany.pm.issue.core.graphql.type.IssueConnection;
import cn.asany.pm.issue.core.service.IssueService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 问题 接口
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class IssueGraphQLQueryResolver implements GraphQLQueryResolver {
  private final IssueService issueService;

  public IssueGraphQLQueryResolver(IssueService issueService) {
    this.issueService = issueService;
  }

  /**
   * 查询单条任务
   *
   * @param id
   * @return
   */
  public Issue issue(Long id) {
    return issueService.findById(id);
  }

  /**
   * 分页查询任务
   *
   * @param filter 筛选条件
   * @param page 页数
   * @param pageSize 每页展示数
   * @param orderBy 排序
   * @return 任务列表
   */
  public IssueConnection issues(IssueWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(issueService.findPage(pageable, where.toFilter()), IssueConnection.class);
  }
}
