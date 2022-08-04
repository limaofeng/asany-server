package cn.asany.pm.issue.type.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 问题类型方案
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
@Table(name = "PM_ISSUE_TYPE_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class IssueTypeScheme extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 方案名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  /** 方案描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 默认问题类型 */
  @JoinColumn(
      name = "DEFAULT_ISSUE_TYPE",
      foreignKey = @ForeignKey(name = "FK_GD_TYPE_SCHEME_DEFAULT_ISSUE_TYPE"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private IssueType defaultType;

  /** 该问题类型方案有哪些问题类型 */
  @ManyToMany(targetEntity = IssueType.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "PM_ISSUE_TYPE_SCHEME_ITEM",
      joinColumns =
          @JoinColumn(
              name = "SCHEME_ID",
              foreignKey = @ForeignKey(name = "FK_ISSUE_TYPE_SCHEME_ITEM_SID")),
      inverseJoinColumns = @JoinColumn(name = "TYPE_ID"),
      foreignKey = @ForeignKey(name = "FK_ISSUE_TYPE_SCHEME_ITEM_TID"))
  @ToString.Exclude
  private List<IssueType> types;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    IssueTypeScheme that = (IssueTypeScheme) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
