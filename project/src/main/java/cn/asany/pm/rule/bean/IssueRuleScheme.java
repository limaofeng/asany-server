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
