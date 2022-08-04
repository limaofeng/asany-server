package cn.asany.pm.rule.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12 @Deprecated 自动派单规则
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_ALLOCATION_RULE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueAllocationRule {

  /** 派单规则ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 规则条件ID */
  @Column(name = "ISSUE_CONDITION", length = 200)
  private Long issueCondition;
  /** 分级类别 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CODE", length = 20)
  private IssueAllocationRuleEnum code;
  /** 规则描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;
  /** 规则优先级 */
  @Column(name = "PRIORITY", length = 20)
  private Long priority;
  /** 是否启用 0禁用 1 启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 选择人员类型 */
  @Column(name = "SELECTION_SCOPE", length = 20)
  private String selectionScope;
}
