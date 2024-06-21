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

import cn.asany.message.api.EmailChannelConfig;
import cn.asany.message.api.IChannelConfig;
import cn.asany.message.api.MSChannelConfig;
import cn.asany.message.api.SMSChannelConfig;
import cn.asany.message.define.domain.enums.TemplateType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.jackson.JSON;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 消息通道定义
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "MSG_MESSAGE_CHANNEL_DEFINITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageChannelDefinition extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  @Column(name = "NAME", nullable = false, length = 20)
  private String name;

  /** 是否为系统内置 */
  @Builder.Default
  @Column(name = "IS_SYSTEM", updatable = false, length = 1)
  private Boolean system = false;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 10, nullable = false, updatable = false)
  private TemplateType type;

  @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
  private String config;

  @Transient
  public IChannelConfig getChannelConfig() {
    if (type == TemplateType.SMS) {
      return JSON.deserialize(config, SMSChannelConfig.class);
    } else if (type == TemplateType.EMAIL) {
      return JSON.deserialize(config, EmailChannelConfig.class);
    } else if (type == TemplateType.MS) {
      return JSON.deserialize(config, MSChannelConfig.class);
    }
    return null;
  }
}
