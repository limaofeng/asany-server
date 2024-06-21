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
package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.domain.Learner;
import cn.asany.cms.learn.domain.enums.LearnerType;
import cn.asany.cms.learn.service.LearnerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import net.asany.jfantasy.graphql.inputs.WhereInput;

public class CourseWhereInput extends WhereInput<CourseWhereInput, Course> {
  private Long learner;
  private LearnerAndTypeInput learnerAndType;

  @JsonProperty(value = "type")
  public void setTypeId(String typeId) {
    filter.equal("type", typeId);
  }

  @JsonProperty(value = "name")
  public void setName(String name) {
    filter.contains("name", name);
  }

  @JsonProperty(value = "publishUser")
  public void setPublishUser(Long employeeId) {
    filter.equal("publishUser", employeeId);
  }

  @JsonProperty(value = "publishDate")
  public void setPublishDate(String publishDate) {
    filter.equal("publishDate", publishDate);
  }

  @JsonProperty(value = "learner")
  public void setLearner(Long employeeId) {
    learner = employeeId;
  }

  private Long learnersLearner;

  @JsonProperty(value = "learnerAndType")
  public void setLearnerAndType(LearnerAndTypeInput learnerAndType) {
    if (learnerAndType.getLearnerType() == LearnerType.elective) {
      filter.equal("learners.type", LearnerType.elective);
      filter.equal("learners.employee.id", learnerAndType.getLearner());
    } else {
      this.learnersLearner = learnerAndType.getLearner();
    }
  }

  @JsonProperty(value = "learnerType")
  public void setLearnerType(LearnerType type) {
    if (type == LearnerType.elective) {
      filter.equal("learners.type", LearnerType.elective);
      filter.equal("learners.employee.id", this.learner);
    } else {
      //            EmployeeService employeeService =
      // SpringBeanUtils.getBeanByType(EmployeeService.class);
      //            Employee employee = employeeService.get(this.learner);
      //            builder.in("learnerScope.scope", employee.getAuthoritys());
    }
  }

  public void setElectiveEmployee(Long employeeId) {
    LearnerService learnerService = SpringBeanUtils.getBeanByType(LearnerService.class);
    //    LearnerScopeService service = SpringBeanUtils.getBeanByType(LearnerScopeService.class);
    //    ids.addAll(
    //        service.findAllByLearnerScope("EMPLOYEE_" + employeeId).stream()
    //            .map(Course::getId)
    //            .collect(Collectors.toList()));
    filter.notIn(
        "id",
        learnerService.findAllByEmployee(employeeId).stream()
            .map(Learner::getCourse)
            .collect(Collectors.toList())
            .stream()
            .map(Course::getId)
            .toArray());
  }
}
