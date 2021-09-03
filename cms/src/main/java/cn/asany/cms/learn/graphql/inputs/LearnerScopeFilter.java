package cn.asany.cms.learn.graphql.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

@Data
public class LearnerScopeFilter {

  PropertyFilterBuilder builder = new PropertyFilterBuilder();

  /** 是否学习完成（100：“完成”） */
  private static final Integer COMPLETE = 100;

  @JsonProperty("course")
  public void setCourse(Long course) {
    builder.equal("course.id", course);
  }

  @JsonProperty("name")
  public void setName(String name) {
    builder.contains("course.learners.employee.name", name);
  }

  @JsonProperty("employeeId")
  public void setEmployeeId(Long id) {
    builder.equal("course.learners.employee.id", id);
  }

  @JsonProperty("department")
  public void setDepartment(Long department) {
    builder.equal("employee.employeePositions.department.id", department);
  }

  @JsonProperty(value = "learningProgress")
  public void setLearningProgress(int learningProgress) {
    if (learningProgress == COMPLETE) {
      builder.equal("course.learners.learningProgress", learningProgress);
    } else {
      builder.notEqual("course.learners.learningProgress", COMPLETE);
    }
  }

  public List<PropertyFilter> build() {
    return this.builder.build();
  }
}
