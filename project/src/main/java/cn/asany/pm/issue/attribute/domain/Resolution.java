package cn.asany.pm.issue.attribute.domain;

import cn.asany.pm.project.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 解决结果 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PM_ISSUE_RESOLUTION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Resolution extends BaseBusEntity {
  /** 任务ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 结果名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 结果描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 排序 */
  @Column(name = "SORT", length = 20)
  private Integer index;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PROJECT", foreignKey = @ForeignKey(name = "FK_ISSUE_RESOLUTION_PROJECT"))
  @ToString.Exclude
  private Project project;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Resolution that = (Resolution) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
