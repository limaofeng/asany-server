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
package cn.asany.message.data.domain;

import cn.asany.message.data.domain.enums.MessageRecipientType;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 消息收件人
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_RECIPIENT")
public class MessageRecipient extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 收件人类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false, length = 20)
  private MessageRecipientType type;

  /** 收件人值 */
  @Column(name = "VALUE", nullable = false, length = 50)
  private String value;

  /** 消息 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MESSAGE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_MESSAGE_RECIPIENT_MID"))
  private Message message;

  public String formatString() {
    return this.type.getName() + MessageRecipientType.DELIMITER + this.value;
  }
}
