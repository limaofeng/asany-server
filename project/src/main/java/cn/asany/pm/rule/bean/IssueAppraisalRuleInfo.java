package cn.asany.pm.rule.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_APPRAISAL_RULE_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueAppraisalRuleInfo {
  /** 自动评价详情ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  @Column(name = "description", length = 200)
  private String description;

  @Column(name = "sort", length = 50)
  private Long sort;
}
