package cn.asany.pm.rule.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 自动派单规则方案
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_RULE__SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueRuleScheme extends BaseBusEntity {

  /** 派单规则ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 方案名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  /** 是否启用 0手动 1 自动 */
  @Column(name = "AUTOMATIC")
  private Boolean automatic;

  /** 该问题类型方案有哪些问题类型 */
  @ManyToMany(targetEntity = IssueAllocationRule.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "PM_ISSUE_RULE_SCHEME_ITEM",
      joinColumns =
          @JoinColumn(
              name = "SCHEME_ID",
              foreignKey = @ForeignKey(name = "FK_ISSUE_RULE_SCHEME_ITEM_SID")),
      inverseJoinColumns = @JoinColumn(name = "RULE_ID"),
      foreignKey = @ForeignKey(name = "FK_ISSUE_RULE_SCHEME_ITEM_TID"))
  private List<IssueAllocationRule> issueRule;
}
