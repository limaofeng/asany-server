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

import cn.asany.base.sms.SMSProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 短信供应商
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "SmsProvider")
@Table(name = "SMS_PROVIDER")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Provider extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false)
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 10, nullable = false, updatable = false)
  private SMSProvider type;

  @Column(name = "NAME", nullable = false, length = 20)
  private String name;

  @Column(name = "DESCRIPTION", nullable = false, length = 500)
  private String description;

  @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
  private String config;
}
