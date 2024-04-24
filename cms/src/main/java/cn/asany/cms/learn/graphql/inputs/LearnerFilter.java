package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.domain.Learner;
import cn.asany.cms.learn.domain.enums.LearnerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 学习者查询条件
 *
 * @author limaofeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LearnerFilter extends WhereInput<LearnerFilter, Learner> {

  private LearnerType type;

  /** 是否学习完成（100：“完成”） */
  private static final Integer COMPLETE = 100;

  @JsonProperty(value = "employee")
  public void setEmployee(Long employee) {
    filter.equal("employee.id", employee);
  }

  @JsonProperty(value = "type")
  public void setType(LearnerType learnerType) {
    this.type = learnerType;
    if (LearnerType.compulsory == this.type) {
      filter.and(filter);
    } else {
      filter.equal("type", learnerType);
    }
  }

  @JsonProperty(value = "name")
  public void setName(String name) {
    if (LearnerType.compulsory == this.type) {
      filter.contains("name", name);
    } else {
      filter.equal("employee.name", name);
    }
  }

  @JsonProperty(value = "department")
  public void setDepartment(Long department) {
    if (LearnerType.compulsory == this.type) {
      filter.equal("employeePositions.department.id", department);
    } else {
      filter.equal("employee.employeePositions.department.id", department);
    }
  }

  @JsonProperty(value = "learningProgress")
  public void setLearningProgress(int learningProgress) {
    if (learningProgress == COMPLETE) {
      filter.equal("learningProgress", learningProgress);
    } else {
      filter.notEqual("learningProgress", COMPLETE);
    }
  }
}
