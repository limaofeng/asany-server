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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "SmsTemplate")
@Table(name = "SMS_TEMPLATE")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "id"})
public class Template extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 模版名称 */
  @Column(name = "NAME", nullable = false, length = 50)
  private String name;

  /** 签名 */
  @Column(name = "SIGN", nullable = false, length = 20)
  private String sign;

  /** 模板号 */
  @Column(name = "CODE", length = 120)
  private String code;

  /** 模版内容 */
  @Column(name = "CONTENT", columnDefinition = "Text")
  private String content;
}
