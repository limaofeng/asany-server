package cn.asany.workflow.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng@msn.com @ClassName: WorkflowScheme @Description: 工作流方案(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_WORKFLOW_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowScheme extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "issue_workflow_scheme_gen")
  @TableGenerator(
      name = "issue_workflow_scheme_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "issue_workflow_scheme:id",
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
