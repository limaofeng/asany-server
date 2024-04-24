package cn.asany.pm.range.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
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
@Table(name = "ISSUE_RANGE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueRange extends BaseBusEntity {
  /** 范围ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 范围编号 */
  @Column(name = "CODE", length = 50)
  private String code;

  /** 范围名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 范围描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    IssueRange that = (IssueRange) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
