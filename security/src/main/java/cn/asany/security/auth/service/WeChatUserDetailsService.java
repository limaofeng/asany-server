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
package cn.asany.security.auth.service;

import cn.asany.security.auth.authentication.WeChatAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.security.core.userdetails.SimpleUserDetailsService;
import net.asany.jfantasy.framework.security.core.userdetails.UserDetails;
import net.asany.jfantasy.framework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 微信获取用户
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class WeChatUserDetailsService
    implements SimpleUserDetailsService<WeChatAuthenticationToken.WeChatCredentials> {

  @Override
  public UserDetails loadUserByToken(WeChatAuthenticationToken.WeChatCredentials credentials)
      throws UsernameNotFoundException {
    String authCode = credentials.getAuthCode();
    return null;
  }
}
