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
package cn.asany.message.define.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Map;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.dao.hibernate.converter.MapConverter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 消息定义提醒
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_DEFINITION_REMINDER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageReminderDefinition extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MESSAGE_DEFINITION_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_MSG_REMINDER_MESSAGE_DEFINITION_ID"))
  private MessageDefinition messageDefinition;

  /** 与模版之间的变量映射表 */
  @Convert(converter = MapConverter.class)
  @Column(name = "MAPPING_VARIABLES", columnDefinition = "Text")
  private Map<String, String> mappingVariables;

  /** 提醒定义 */
  @ManyToOne(targetEntity = ReminderDefinition.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "REMINDER_DEFINITION_ID",
      foreignKey = @ForeignKey(name = "FK_MSG_REMINDER_REMINDER_DEFINITION_ID"))
  @ToString.Exclude
  private ReminderDefinition reminderDefinition;
}
