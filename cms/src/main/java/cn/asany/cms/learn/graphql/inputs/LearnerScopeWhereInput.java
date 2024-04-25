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

import cn.asany.cms.learn.domain.Learner;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 学习者范围查询条件
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LearnerScopeWhereInput extends WhereInput<LearnerScopeWhereInput, Learner> {

  /** 是否学习完成（100：“完成”） */
  private static final Integer COMPLETE = 100;

  @JsonProperty("course")
  public void setCourse(Long course) {
    filter.equal("course.id", course);
  }

  @JsonProperty("name")
  public void setName(String name) {
    filter.contains("course.learners.employee.name", name);
  }

  @JsonProperty("employeeId")
  public void setEmployeeId(Long id) {
    filter.equal("course.learners.employee.id", id);
  }

  @JsonProperty("department")
  public void setDepartment(Long department) {
    filter.equal("employee.employeePositions.department.id", department);
  }

  @JsonProperty(value = "learningProgress")
  public void setLearningProgress(int learningProgress) {
    if (learningProgress == COMPLETE) {
      filter.equal("course.learners.learningProgress", learningProgress);
    } else {
      filter.notEqual("course.learners.learningProgress", COMPLETE);
    }
  }
}
