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
package cn.asany.security.auth.authentication;

import lombok.Builder;
import lombok.Data;
import net.asany.jfantasy.framework.security.authentication.SimpleAuthenticationToken;

/**
 * 微信认证凭证
 *
 * @author limaofeng
 */
public class WeChatAuthenticationToken
    extends SimpleAuthenticationToken<WeChatAuthenticationToken.WeChatCredentials> {

  public WeChatAuthenticationToken(WeChatCredentials credentials) {
    super(credentials);
  }

  /** 微信凭证 */
  @Data
  @Builder
  public static class WeChatCredentials {

    /** 授权码 */
    private String authCode;

    /** 通过手机号绑定钉钉用户 */
    private Boolean connected;
  }
}
