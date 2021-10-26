package cn.asany.workflow.core.bean;

import cn.asany.workflow.screen.bean.Screen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author penghanying @ClassName: IssueWorkflowStepTransition @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_WORKFLOW_STEP_TRANSITION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowStepTransition extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 目标步骤 */
  @JsonProperty("destination")
  @JoinColumn(
      name = "TARGET_STEP",
      foreignKey = @ForeignKey(name = "FK_WORKFLOW_STEP_TRANSITION_TARGET"))
  @ManyToOne(fetch = FetchType.LAZY)
  private WorkflowStep destination;

  /** 界面 */
  @JsonProperty("view")
  @JoinColumn(
      name = "SCREEN",
      foreignKey = @ForeignKey(name = "FK_WORKFLOW_STEP_TRANSITION_SCREEN"))
  @ManyToOne(fetch = FetchType.LAZY)
  private Screen screen;

  /** 该步骤 */
  @JsonProperty("step")
  @JoinColumn(
      name = "CURRENT_STEP",
      foreignKey = @ForeignKey(name = "FK_WORKFLOW_STEP_TRANSITION_CURRENT"))
  @ManyToOne(fetch = FetchType.LAZY)
  private WorkflowStep current;

  @OneToMany(
      mappedBy = "transition",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<WorkflowStepLog> schedules;

  @OneToMany(
      mappedBy = "transition",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<WorkflowStepTransitionCondition> conditions;
}
