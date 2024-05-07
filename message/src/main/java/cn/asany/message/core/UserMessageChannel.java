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
import cn.asany.message.api.MessageChannel;
import cn.asany.message.api.SimpleMessage;
import cn.asany.message.data.domain.UserMessage;
import cn.asany.message.data.service.UserMessageService;
import cn.asany.security.core.domain.User;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.core.userdetails.UserDetailsService;
import net.asany.jfantasy.framework.util.HandlebarsTemplateUtils;
import net.asany.jfantasy.framework.util.common.StringUtil;

/**
 * WebPush 消息发送者
 *
 * @author limaofeng
 */
@Slf4j
public class UserMessageChannel implements MessageChannel<SimpleMessage> {

  private final UserMessageService userMessageService;
  private final MSChannelConfig config;

  private final UserDetailsService<LoginUser> userDetailsService;

  public UserMessageChannel(
      UserDetailsService<LoginUser> userDetailsService,
      UserMessageService userMessageService,
      MSChannelConfig config) {
    this.userMessageService = userMessageService;
    this.userDetailsService = userDetailsService;
    this.config = config;
    log.info("WebPushMessageSender init success, config: {}", config);
  }

  @Override
  public void send(SimpleMessage message) {
    if (StringUtil.isBlank(message.getSubject())) {
      throw new RuntimeException("subject is blank");
    }
    if (StringUtil.isBlank(message.getText())) {
      throw new RuntimeException("text is blank");
    }
    for (String recipient : message.getTo()) {
      LoginUser loginUser = userDetailsService.loadUserById(Long.valueOf(recipient)).join();

      log.info("send message to {}", loginUser.getUsername());

      Map<String, Object> data = new HashMap<>(message.getTemplateParams());
      data.put("user", loginUser);

      String title = HandlebarsTemplateUtils.processTemplateIntoString(message.getSubject(), data);
      String content = HandlebarsTemplateUtils.processTemplateIntoString(message.getText(), data);

      userMessageService.save(
          UserMessage.builder()
              .read(false)
              .user(User.builder().id(Long.valueOf(recipient)).build())
              .title(title)
              .content(content)
              .type(message.getOriginalMessageType())
              .message(message.getOriginalMessage())
              .uri(message.getUri())
              .build());
    }
  }
}
