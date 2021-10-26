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
 * @author penghanying @ClassName: WorkflowScheme @Description: 工作流方案(这里用一句话描述这个类的作用)
 * @date 2019/5/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GD_ISSUE_WORKFLOW_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowScheme extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "gd_issue_workflow_scheme_gen")
  @TableGenerator(
      name = "gd_issue_workflow_scheme_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "gd_issue_workflow_scheme:id",
      valueColumnName = "gen_value")
  private Long id;

  /** 工作流方案名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  /** 工作流方案描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 工作流 */
  @JsonProperty("defaultWorkflow")
  @JoinColumn(name = "WID", foreignKey = @ForeignKey(name = "FK_ISSUE_WORKFLOW_SCHEME_DEFAULT_WID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private Workflow defaultWorkflow;
}
