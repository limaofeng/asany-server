package cn.asany.pm.workflow.bean;

import cn.asany.pm.workflow.bean.enums.ConditionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 权限条件
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
@Table(name = "ISSUE_WORKFLOW_STEP_TRANSITION_CONDITION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowStepTransitionCondition extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
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
  @ToString.Exclude
  private WorkflowStepTransition transition;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    WorkflowStepTransitionCondition that = (WorkflowStepTransitionCondition) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
