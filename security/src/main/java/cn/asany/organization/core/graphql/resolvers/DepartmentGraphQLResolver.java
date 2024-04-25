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
package cn.asany.organization.core.graphql.resolvers;

import cn.asany.organization.core.domain.Department;
import cn.asany.organization.core.domain.Job;
import cn.asany.organization.core.graphql.enums.DepartmentIdType;
import cn.asany.organization.core.service.DepartmentService;
import cn.asany.organization.relationship.domain.Position;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 部门
 *
 * @author limaofeng
 */
@Component
public class DepartmentGraphQLResolver implements GraphQLResolver<Department> {

  private final DepartmentService departmentService;

  public DepartmentGraphQLResolver(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  public String id(Department department, DepartmentIdType idType) {
    if (idType == DepartmentIdType.ID) {
      return department.getId().toString();
    }
    return null;
  }

  public String fullName(Department department) {
    if (department == null) {
      return null;
    }
    List<Department> parents = this.parents(department);
    if (parents.size() > 0) {
      return parents.stream().map(Department::getName).collect(Collectors.joining("."));
    }
    return null;
  }

  public List<Department> parents(Department department) {
    List<Department> departments = new ArrayList<>();
    for (String id : StringUtil.tokenizeToStringArray(department.getPath(), "/")) {
      if (Long.valueOf(id).equals(department.getId())) {
        continue;
      }
      departments.add(this.departmentService.get(Long.valueOf(id)));
    }
    return departments;
  }

  public List<Job> jobs(Department department) {
    return department.getPositions().stream().map(Position::getJob).collect(Collectors.toList());
  }
}
