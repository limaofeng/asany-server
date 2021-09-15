package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.bean.Course;
import cn.asany.cms.learn.bean.Learner;
import cn.asany.cms.learn.bean.enums.LearnerType;
import cn.asany.cms.learn.service.LearnerScopeService;
import cn.asany.cms.learn.service.LearnerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.SpringBeanUtils;

@Data
public class CourseFilter {
  private Long learner;
  private LearnerAndTypeInput learnerAndType;
  private PropertyFilterBuilder builder = new PropertyFilterBuilder();

  @JsonProperty(value = "type")
  public void setTypeId(String typeId) {
    builder.equal("type", typeId);
  }

  @JsonProperty(value = "name")
  public void setName(String name) {
    builder.contains("name", name);
  }

  @JsonProperty(value = "publishUser")
  public void setPublishUser(Long employeeId) {
    builder.equal("publishUser", employeeId);
  }

  @JsonProperty(value = "publishDate")
  public void setPublishDate(String publishDate) {
    builder.equal("publishDate", publishDate);
  }

  @JsonProperty(value = "learner")
  public void setLearner(Long employeeId) {
    learner = employeeId;
  }

  private Long learnersLearner;

  @JsonProperty(value = "learnerAndType")
  public void setLearnerAndType(LearnerAndTypeInput learnerAndType) {
    if (learnerAndType.getLearnerType() == LearnerType.elective) {
      builder.equal("learners.type", LearnerType.elective);
      builder.equal("learners.employee.id", learnerAndType.getLearner());
    } else {
      this.learnersLearner = learnerAndType.getLearner();
    }
  }

  @JsonProperty(value = "learnerType")
  public void setLearnerType(LearnerType type) {
    if (type == LearnerType.elective) {
      builder.equal("learners.type", LearnerType.elective);
      builder.equal("learners.employee.id", this.learner);
    } else {
      //            EmployeeService employeeService =
      // SpringBeanUtils.getBeanByType(EmployeeService.class);
      //            Employee employee = employeeService.get(this.learner);
      //            builder.in("learnerScope.scope", employee.getAuthoritys());
    }
  }

  public void setElectiveEmployee(Long employeeId) {
    LearnerService learnerService = SpringBeanUtils.getBeanByType(LearnerService.class);
    List<Long> ids =
        learnerService.findAllByEmployee(employeeId).stream()
            .map(Learner::getCourse)
            .collect(Collectors.toList())
            .stream()
            .map(Course::getId)
            .collect(Collectors.toList());
    LearnerScopeService service = SpringBeanUtils.getBeanByType(LearnerScopeService.class);
    ids.addAll(
        service.findAllByLearnerScope("EMPLOYEE_" + employeeId).stream()
            .map(Course::getId)
            .collect(Collectors.toList()));
    builder.notIn("id", ids.toArray());
  }

  public List<PropertyFilter> build() {
    return this.builder.build();
  }
}
