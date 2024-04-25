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
import org.hibernate.annotations.GenericGenerator;

/**
 * 短信验证码
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-4 下午01:56:29
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SMS_CAPTCHA")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "config"})
public class Captcha extends BaseBusEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID")
  @Column(name = "ID", nullable = false, updatable = false, length = 32)
  private String id;

  /** 配置信息 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CONFIG_ID", foreignKey = @ForeignKey(name = "FK_CONFIG_CAPTCHA"))
  private CaptchaConfig config;

  /** 绑定的会话ID */
  @Column(name = "SESSION_ID", length = 120, nullable = false)
  private String sessionId;

  /** 生成的CODE值 */
  @Column(name = "VALUE", length = 120)
  private String value;

  /** 手机号 */
  @Column(name = "PHONE", length = 120, nullable = false)
  private String phone;

  /** 已经重试的次数 */
  @Column(name = "RETRY")
  private int retry;
}
