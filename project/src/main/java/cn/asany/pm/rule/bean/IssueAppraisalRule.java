/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

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
  @TableGenerator
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
