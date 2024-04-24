package cn.asany.workflow.core.domain;

import cn.asany.workflow.state.domain.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 工作流 实例
 *
 * @author limaofeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_WORKFLOW_INSTANCE")
public class WorkflowInstance extends BaseBusEntity {

  /** 主键 */
  @Id
  @Column(name = "ID")
  private Long id;

  /** 阶段id */
  @JoinColumn(name = "STATE", foreignKey = @ForeignKey(name = "FK_WORKFLOW_INSTANCE_STATE"))
  @ManyToOne(fetch = FetchType.LAZY)
  private State state;

  /** 步骤id */
  @JoinColumn(name = "STEP", foreignKey = @ForeignKey(name = "FK_STAGE_WORKFLOW_INSTANCE_STEP"))
  @ManyToOne(fetch = FetchType.LAZY)
  private WorkflowStep step;
}
