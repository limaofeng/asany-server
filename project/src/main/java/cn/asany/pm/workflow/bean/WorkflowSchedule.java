package cn.asany.pm.workflow.bean;

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.project.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 保存每一步操作的数据
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "WORKFLOW_SCHEDULE")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowSchedule extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 问题的id */
  @JsonProperty("issue")
  @JoinColumn(name = "issue", foreignKey = @ForeignKey(name = "FK_WORKFLOW_SCHEDULE_ISSUE"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Issue issue;

  /** 操作id */
  @JsonProperty("tran")
  @JoinColumn(name = "tran", foreignKey = @ForeignKey(name = "FK_WORKFLOW_SCHEDULE_STEP_TRAN"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private WorkflowStepTransition transition;

  /** 项目id */
  @JsonProperty("project")
  @JoinColumn(name = "project", foreignKey = @ForeignKey(name = "FK_WORKFLOW_SCHEDULE_PROJECT"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Project project;

  /** 经办人 */
  @Column(name = "ASSIGNEE", length = 100)
  private Long assignee;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    WorkflowSchedule that = (WorkflowSchedule) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
