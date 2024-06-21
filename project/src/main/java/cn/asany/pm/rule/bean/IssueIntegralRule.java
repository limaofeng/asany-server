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
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_INTEGRAL_RULE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueIntegralRule extends BaseBusEntity {
  /** 范围ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 条件对应ID */
  @Column(name = "CONDITION_ID")
  private Long conditionId;

  /** 分级类别 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CODE", length = 20)
  private IssueIntegralRuleEnum code;

  /** 分级名称 */
  @Column(name = "NAME")
  private String name;

  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;

  /** 分值 */
  @Column(name = "SCORE")
  private Integer score;
}
