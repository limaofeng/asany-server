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

import cn.asany.message.api.EmailChannelConfig;
import cn.asany.message.api.MessageChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageChannelBuilder
    implements MessageChannelBuilder<EmailMessageChannel, EmailChannelConfig> {

  @Override
  public boolean supports(Class<EmailChannelConfig> clazz) {
    return EmailChannelConfig.class.isAssignableFrom(clazz);
  }

  @Override
  public EmailMessageChannel build(EmailChannelConfig config) {
    return new EmailMessageChannel(config);
  }
}
