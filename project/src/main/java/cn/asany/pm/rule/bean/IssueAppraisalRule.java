package cn.asany.pm.rule.bean;

import cn.asany.pm.issue.attribute.domain.Status;
import cn.asany.pm.rule.bean.databind.IssueAppraisalRuleInfoDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_APPRAISAL_RULE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueAppraisalRule {
  /** 自带评价规则ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ISSUE_STATUS_ID",
      foreignKey = @ForeignKey(name = "FK_ISSUE_APPRAISAL_RULE_STATUS"))
  private Status status;

  @Column(name = "TIMES", length = 200)
  private Long times;

  @JsonDeserialize(using = IssueAppraisalRuleInfoDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ISSUE_APPRAISAL_RULE_INFO_ID",
      foreignKey = @ForeignKey(name = "FK_ISSUE_APPRAISAL_RULE_INFO"))
  private IssueAppraisalRuleInfo appraisalRuleInfo;

  /** 是否启用 0禁用 1 启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
}
