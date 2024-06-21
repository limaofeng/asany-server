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
package cn.asany.sms.domain;

import cn.asany.base.sms.MessageStatus;
import cn.asany.base.sms.ShortMessageInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 短信消息
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SMS_MESSAGE")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "config"})
public class ShortMessage extends BaseBusEntity implements ShortMessageInfo {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 短信签名 */
  @Column(name = "SIGN", length = 50)
  private String sign;

  /** 模版 */
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "TEMPLATE_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_TEMPLATE_ID"))
  private Template template;

  /** 发送内容 */
  @Column(name = "CONTENT", length = 200)
  private String content;

  /** 描述 */
  @Column(name = "NOTES", columnDefinition = "Text")
  private String notes;

  /** 发送状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 10)
  private MessageStatus status;

  /** 接收者手机号码 */
  @ElementCollection
  @CollectionTable(
      name = "SMS_MESSAGE_PHONES",
      foreignKey = @ForeignKey(name = "FK_SMS_MESSAGE_PHONE"),
      joinColumns = @JoinColumn(name = "MSG_ID"))
  @Column(name = "PHONE")
  private List<String> phones;

  /** 延时发送的时间 */
  @Column(name = "DELAY", nullable = false)
  private Long delay;

  /** 短信服务商 */
  @Column(name = "PROVIDER", nullable = false, updatable = false, length = 50)
  private String provider;

  /** 如果为短信验证码时，该字段不为空 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CONFIG_ID", foreignKey = @ForeignKey(name = "FK_SMS_CONFIG"))
  private CaptchaConfig config;

  @Transient
  @Override
  public String getTemplateId() {
    return this.template != null ? this.template.getCode() : null;
  }

  @Transient
  @Override
  public String getConfigId() {
    return this.getConfig() != null ? this.getConfig().getId() : null;
  }
}
