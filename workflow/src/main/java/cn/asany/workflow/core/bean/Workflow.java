package cn.asany.workflow.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 流程
 *
 * @author limaofeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_WORKFLOW")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Workflow extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  private Long id;

  /** 工作流名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 工作流描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 该工作流包含的步骤 */
  @OneToMany(
      mappedBy = "workflow",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<WorkflowStep> steps;
}
