package cn.asany.pm.workflow.bean;

import cn.asany.pm.issue.attribute.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 步骤
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
@Table(name = "ISSUE_WORKFLOW_STEP")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowStep extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 状态id */
  @JsonProperty("state")
  @JoinColumn(name = "SID", foreignKey = @ForeignKey(name = "FK_GD_ISSUE_WORKFLOW_STEP_SID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Status state;

  @JoinColumn(name = "WID", foreignKey = @ForeignKey(name = "FK_ISSUE_WORKFLOW_STEP_WID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Workflow workflow;

  @OneToMany(
      mappedBy = "destination",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<WorkflowStepTransition> transitions;

  @OneToMany(
      mappedBy = "step",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<WorkflowStepTransition> step;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    WorkflowStep that = (WorkflowStep) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
