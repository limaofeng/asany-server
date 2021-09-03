package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.bean.enums.LearnerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

@Data
public class LearnerFilter {

  private PropertyFilterBuilder builder = new PropertyFilterBuilder();

  private PropertyFilterBuilder employeeBuilder = PropertyFilter.builder();

  private LearnerType type;

  /** 是否学习完成（100：“完成”） */
  private static final Integer COMPLETE = 100;

  @JsonProperty(value = "employee")
  public void setEmployee(Long employee) {
    builder.equal("employee.id", employee);
  }

  @JsonProperty(value = "type")
  public void setType(LearnerType learnerType) {
    this.type = learnerType;
    if (LearnerType.compulsory == this.type) {
      builder.and(employeeBuilder);
    } else {
      builder.equal("type", learnerType);
    }
  }

  @JsonProperty(value = "name")
  public void setName(String name) {
    if (LearnerType.compulsory == this.type) {
      employeeBuilder.contains("name", name);
    } else {
      builder.equal("employee.name", name);
    }
  }

  @JsonProperty(value = "department")
  public void setDepartment(Long department) {
    if (LearnerType.compulsory == this.type) {
      employeeBuilder.equal("employeePositions.department.id", department);
    } else {
      builder.equal("employee.employeePositions.department.id", department);
    }
  }

  @JsonProperty(value = "learningProgress")
  public void setLearningProgress(int learningProgress) {
    if (learningProgress == COMPLETE) {
      builder.equal("learningProgress", learningProgress);
    } else {
      builder.notEqual("learningProgress", COMPLETE);
    }
  }

  public List<PropertyFilter> build() {
    return this.builder.build();
  }
}
