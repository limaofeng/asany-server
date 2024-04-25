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

import cn.asany.cms.learn.domain.LessonRecord;
import cn.asany.cms.learn.domain.enums.LearnerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.asany.jfantasy.graphql.inputs.WhereInput;

public class LessonRecordWhereInput extends WhereInput<LessonRecordWhereInput, LessonRecord> {

  @JsonProperty(value = "employee")
  public void setEmployee(Long employee) {
    filter.equal("learner.employee.id", employee);
  }

  @JsonProperty(value = "type")
  public void setType(LearnerType learnerType) {
    filter.equal("learner.type", learnerType);
  }

  @JsonProperty(value = "course")
  public void setCourse(Long course) {
    filter.equal("course", course);
  }

  @JsonProperty(value = "lessonScheduleType")
  public void setLessonScheduleType(String lessonScheduleType) {
    filter.equal("learner.lessonScheduleType", lessonScheduleType);
  }
}
