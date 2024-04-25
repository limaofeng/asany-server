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
package cn.asany.pm.project.graphql;

import cn.asany.pm.project.convert.ProjectConverter;
import cn.asany.pm.project.domain.Project;
import cn.asany.pm.project.graphql.input.ProjectCreateInput;
import cn.asany.pm.project.graphql.input.ProjectWhereInput;
import cn.asany.pm.project.graphql.type.ProjectConnection;
import cn.asany.pm.project.service.ProjectService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Optional;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 项目接口
 *
 * @author limaofeng
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class ProjectGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final ProjectService projectService;
  private final ProjectConverter projectConverter;

  public ProjectGraphQLRootResolver(
      ProjectService projectService, ProjectConverter projectConverter) {
    this.projectService = projectService;
    this.projectConverter = projectConverter;
  }

  /**
   * 新建项目
   *
   * @param input 输入对象
   * @return Project
   */
  public Project createProject(ProjectCreateInput input) {
    Project project = this.projectConverter.toProject(input);
    return this.projectService.save(project);
  }

  public Optional<Project> project(Long id) {
    return projectService.get(id);
  }

  /**
   * 查询所有项目
   *
   * @param where 过滤
   * @param page 页码
   * @param pageSize 每页显示数据条数
   * @param orderBy 排序
   * @return ProjectConnection
   */
  public ProjectConnection projects(ProjectWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        projectService.findPage(pageable, where.toFilter()), ProjectConnection.class);
  }
}
