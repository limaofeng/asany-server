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
package cn.asany.cms.learn.graphql.types;

import cn.asany.cms.learn.domain.Course;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

/**
 * 课程分页对象
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseConnection extends BaseConnection<CourseConnection.CourseEdge, Course> {

  private List<CourseEdge> edges;

  @Data
  public static class CourseEdge implements Edge<Course> {
    private String cursor;
    private Course node;
  }
}
