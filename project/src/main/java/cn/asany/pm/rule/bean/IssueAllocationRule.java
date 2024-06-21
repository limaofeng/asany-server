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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

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
  @TableGenerator
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
