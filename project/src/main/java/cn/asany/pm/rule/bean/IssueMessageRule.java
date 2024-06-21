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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_MESSAGE_RULE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IssueMessageRule {
  /** 范围ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 消息规则名称 */
  @Column(name = "NAME")
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;

  /** 消息规则内容 */
  @Column(name = "CONTENT")
  private String content;

  /** 对应的操作 */
  @Column(name = "OPERATION")
  private String operation;

  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;

  /** 是否多次提醒 */
  @Column(name = "MESSAGE_LOOP")
  private Boolean messageLoop;

  /** 消息间隔时长 */
  @Column(name = "TIME")
  private Long time;

  /** 提醒人员类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "MESSAGE_RULE", length = 20)
  private MessageRuleEum messageRuleEum;
}
