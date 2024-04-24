package cn.asany.pm.issue.type.domain;

import cn.asany.pm.issue.core.domain.Issue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.SoftDeletableBaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

/**
 * 任务类型实体
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
@Table(name = "PM_ISSUE_TYPE")
@Where(clause = "deleted = false")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class IssueType extends SoftDeletableBaseBusEntity {
  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 任务id */
  @OneToMany(
      mappedBy = "type",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @ToString.Exclude
  private List<Issue> issue;

  /** 类型名称 例如：工单 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 类型描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 排序字段 */
  @Column(name = "SORT")
  private Integer sort;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    IssueType issueType = (IssueType) o;
    return id != null && Objects.equals(id, issueType.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
