package cn.asany.pm.issue.attribute.domain;

import cn.asany.pm.issue.attribute.domain.enums.StatusCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 工单状态
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
@Entity(name = "IssueStatus")
@Table(name = "PM_ISSUE_STATUS")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Status extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 状态排序字段 */
  @Column(name = "SORT")
  private Integer index;

  /** 状态归类 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", length = 20)
  private StatusCategory category;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Status that = (Status) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
