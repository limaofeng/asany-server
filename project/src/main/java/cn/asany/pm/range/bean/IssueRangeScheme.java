package cn.asany.pm.range.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_RANGE_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueRangeScheme {

  /** 范围方案ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 范围方案名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  /** 该问题类型方案有哪些问题类型 */
  @ManyToMany(targetEntity = IssueRange.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "PM_ISSUE_RANGE_SCHEME_ITEM",
      joinColumns =
          @JoinColumn(
              name = "SCHEME_ID",
              foreignKey = @ForeignKey(name = "FK_ISSUE_RANGE_SCHEME_ITEM_SID")),
      inverseJoinColumns = @JoinColumn(name = "RULE_ID"),
      foreignKey = @ForeignKey(name = "FK_ISSUE_RANGE_SCHEME_ITEM_TID"))
  private List<IssueRange> issueRanges;
}
