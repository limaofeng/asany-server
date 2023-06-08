package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.domain.LessonRecord;
import cn.asany.cms.learn.domain.enums.LearnerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

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
