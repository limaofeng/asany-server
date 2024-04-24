package cn.asany.pm.issue.priority.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 优先级方案
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PM_ISSUE_PRIORITY_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PriorityScheme extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  @Column(name = "NAME", length = 100)
  private String name;

  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DEFAULT_PRIORITY",
      foreignKey = @ForeignKey(name = "FK_ISSUE_PRIORITY_SCHEME_DEFAULT"))
  @ToString.Exclude
  private Priority defaultPriority;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @JoinTable(
      name = "PM_ISSUE_PRIORITY_SCHEME_ITEM",
      joinColumns =
          @JoinColumn(
              name = "SCHEME_ID",
              foreignKey = @ForeignKey(name = "FK_ISSUE_PRIORITY_SCHEME_ITEM_SCHEME")),
      inverseJoinColumns = @JoinColumn(name = "PRIORITY_ID"),
      foreignKey = @ForeignKey(name = "FK_ISSUE_PRIORITY_SCHEME_ITEM_PRIORITY"))
  @ToString.Exclude
  private List<Priority> priorities;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    PriorityScheme that = (PriorityScheme) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
