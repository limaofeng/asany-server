package cn.asany.workflow.core.domain;

import cn.asany.workflow.core.domain.enums.WorkflowStepType;
import cn.asany.workflow.state.domain.State;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 流程步骤(节点)
 *
 * @author limaofeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_WORKFLOW_STEP")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowStep extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  private WorkflowStepType type;

  /** 状态id */
  @JoinColumn(name = "state", foreignKey = @ForeignKey(name = "FK_GD_ISSUE_WORKFLOW_STEP_SID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private State state;

  @JoinColumn(name = "WORKFLOW", foreignKey = @ForeignKey(name = "FK_ISSUE_WORKFLOW_STEP_WID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private Workflow workflow;

  @OneToMany(
      mappedBy = "destination",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<WorkflowStepTransition> transitions;

  @OneToMany(
      mappedBy = "step",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<WorkflowStepTransition> step;
}
