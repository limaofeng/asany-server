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
package cn.asany.message.core;

import cn.asany.message.api.MSChannelConfig;
import cn.asany.message.api.MessageChannelBuilder;
import cn.asany.message.data.service.UserMessageService;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * WebPush 消息发送者构建器
 *
 * @author limaofeng
 */
@Component
public class UserMessageChannelBuilder
    implements MessageChannelBuilder<UserMessageChannel, MSChannelConfig> {

  private final UserMessageService userMessageService;

  private final UserDetailsService<LoginUser> userDetailsService;

  public UserMessageChannelBuilder(
      UserMessageService userMessageService, UserDetailsService<LoginUser> userDetailsService) {
    this.userMessageService = userMessageService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public boolean supports(Class<MSChannelConfig> clazz) {
    return MSChannelConfig.class.isAssignableFrom(clazz);
  }

  @Override
  public UserMessageChannel build(MSChannelConfig config) {
    return new UserMessageChannel(this.userDetailsService, userMessageService, config);
  }
}
