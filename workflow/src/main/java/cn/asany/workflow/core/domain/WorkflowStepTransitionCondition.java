package cn.asany.workflow.core.domain;

import cn.asany.workflow.core.domain.enums.ConditionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 权限条件
 *
 * @author limaofeng
 * @date 2022/7/28 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_WORKFLOW_STEP_TRANSITION_CONDITION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowStepTransitionCondition extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "issue_permission_condition_gen")
  @TableGenerator(
      name = "issue_permission_condition_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "issue_permission_condition:id",
      valueColumnName = "gen_value")
  private Long id;

  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private ConditionType type;

  /** 权限 权限的编码code */
  @Column(name = "VALUE", length = 50)
  private String value;

  /** 操作 */
  @JoinColumn(name = "TID", foreignKey = @ForeignKey(name = "FK_ISSUE_PERMISSION_CONDITION_TID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private WorkflowStepTransition transition;
}
