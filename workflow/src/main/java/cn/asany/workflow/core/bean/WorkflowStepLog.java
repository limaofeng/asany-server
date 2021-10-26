package cn.asany.workflow.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 工作流任务
 *
 * @author limaofeng
 * @date 2019/5/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_WORKFLOW_STEP_LOG")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowStepLog extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  private Long id;

  /** 问题的id */
  @JoinColumn(name = "instance", foreignKey = @ForeignKey(name = "FK_WORKFLOW_SCHEDULE_ISSUE"))
  @ManyToOne(fetch = FetchType.LAZY)
  private WorkflowInstance instance;

  /** 操作id */
  @JsonProperty("tran")
  @JoinColumn(
      name = "transition",
      foreignKey = @ForeignKey(name = "FK_WORKFLOW_SCHEDULE_STEP_TRANSITION"))
  @ManyToOne(fetch = FetchType.LAZY)
  private WorkflowStepTransition transition;

  /** 项目id */
  @JoinColumn(name = "instance", foreignKey = @ForeignKey(name = "FK_WORKFLOW_STEP_LOG_INSTANCE"))
  @ManyToOne(fetch = FetchType.LAZY)
  private WorkflowInstance instance;

  /** 经办人 */
  @Column(name = "ASSIGNEE", length = 100)
  private Long assignee;
}
