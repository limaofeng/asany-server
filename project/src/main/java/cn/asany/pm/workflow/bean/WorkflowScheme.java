package cn.asany.pm.workflow.bean;

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
 * 工作流方案
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
@Table(name = "ISSUE_WORKFLOW_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowScheme extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
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
  @ToString.Exclude
  private Workflow defaultWorkflow;

  @OneToMany(
      mappedBy = "workflowScheme",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<WorkflowAndIssueType> workflows;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    WorkflowScheme that = (WorkflowScheme) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
