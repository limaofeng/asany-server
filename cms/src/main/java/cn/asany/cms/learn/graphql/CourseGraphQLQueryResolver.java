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
package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.graphql.inputs.CourseWhereInput;
import cn.asany.cms.learn.graphql.types.CourseConnection;
import cn.asany.cms.learn.service.CourseService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CourseGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private CourseService courseService;

  public Course course(Long id) {
    return courseService.findById(id);
  }

  public CourseConnection courses(CourseWhereInput where, int page, int pageSize) {
    PropertyFilter propertyFilter = where.toFilter();
    return Kit.connection(
        courseService.findPage(PageRequest.of(page - 1, pageSize, Sort.by("top")), propertyFilter),
        CourseConnection.class);
  }
}
