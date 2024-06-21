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
package cn.asany.openapi.configs;

import cn.asany.openapi.IOpenApiConfig;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.factory.WeixinSessionFactory;
import cn.asany.weixin.framework.session.WeixinApp;
import cn.asany.weixin.framework.session.WeixinAppType;
import cn.asany.weixin.framework.session.WeixinSession;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;

/**
 * 微信配置
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeixinConfig implements IOpenApiConfig, WeixinApp {
  /** App ID */
  private String appId;

  /** 密钥 */
  private String appSecret;

  /** 公众号类型 */
  private WeixinAppType type;

  /** 原始ID */
  private String primitiveId;

  /** 微信服务器配置的token */
  private String token;

  /** 微信生成的 ASEKey */
  private String aesKey;

  /**
   * 代理ID<br>
   * 企业号才需要配置该属性
   */
  private Integer agentId;

  /** 微信 Session */
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private WeixinSession session;

  @Override
  @JsonIgnore
  public String getId() {
    return this.appId;
  }

  @Override
  @JsonIgnore
  public String getSecret() {
    return this.appSecret;
  }

  @JsonIgnore
  public WeixinSession getSession() throws WeixinException {
    if (session == null) {
      WeixinSessionFactory weixinSessionFactory =
          SpringBeanUtils.getBean(WeixinSessionFactory.class);
      session = weixinSessionFactory.openSession(this.appId);
    }
    return session;
  }
}
