package cn.asany.pm.workflow.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 工作流
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
@Table(name = "ISSUE_WORKFLOW")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Workflow extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
  @ToString.Exclude
  private List<WorkflowStep> steps;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Workflow that = (Workflow) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
