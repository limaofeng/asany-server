package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.bean.enums.LearnerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

@Data
public class LessonRecordFilter {

  private PropertyFilterBuilder builder = new PropertyFilterBuilder();

  @JsonProperty(value = "employee")
  public void setEmployee(Long employee) {
    builder.equal("learner.employee.id", employee);
  }

  @JsonProperty(value = "type")
  public void setType(LearnerType learnerType) {
    builder.equal("learner.type", learnerType);
  }

  @JsonProperty(value = "course")
  public void setCourse(Long course) {
    builder.equal("course", course);
  }

  @JsonProperty(value = "lessonScheduleType")
  public void setLessonScheduleType(String lessonScheduleType) {
    builder.equal("learner.lessonScheduleType", lessonScheduleType);
  }

  public List<PropertyFilter> build() {
    return this.builder.build();
  }
}
