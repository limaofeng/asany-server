package cn.asany.pm.issue.priority.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 任务优先级
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PM_ISSUE_PRIORITY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Priority extends BaseBusEntity {
  /** 任务优先级ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 任务优先级icon */
  @Column(name = "ICON", length = 50)
  private String icon;

  /** 任务优先级名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  /** 任务优先级排序 */
  @Column(name = "SORT", length = 50)
  private Integer index;

  /** 颜色 */
  @Column(name = "COLOR")
  private String color;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Priority that = (Priority) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
