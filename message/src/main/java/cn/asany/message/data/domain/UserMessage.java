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

import cn.asany.message.define.domain.MessageType;
import cn.asany.security.core.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户收到的消息
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_USER_MESSAGE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserMessage extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  @ManyToOne(targetEntity = MessageType.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_USER_MESSAGE_TYPE"))
  @ToString.Exclude
  private MessageType type;

  /** 资源标识符 */
  @Column(name = "URI", length = 150, updatable = false)
  private String uri;

  /** 标题 */
  @Column(name = "TITLE", length = 150, updatable = false)
  private String title;

  /** 内容 */
  @Column(name = "CONTENT", length = 500, updatable = false)
  private String content;

  /** 原始消息 */
  @ManyToOne(targetEntity = Message.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "MESSAGE_ID", foreignKey = @ForeignKey(name = "FK_USER_MESSAGE_MID"))
  @ToString.Exclude
  private Message message;

  /** 已读标识 */
  @Builder.Default
  @Column(name = "`READ`", nullable = false)
  private Boolean read = false;

  /** 用户 */
  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "USER", foreignKey = @ForeignKey(name = "FK_USER_MESSAGE_UID"))
  private User user;
}
