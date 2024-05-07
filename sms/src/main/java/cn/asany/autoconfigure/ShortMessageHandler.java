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
package cn.asany.autoconfigure;

import cn.asany.base.sms.*;
import cn.asany.message.api.*;
import cn.asany.sms.provider.AliyunSMSProviderConfig;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信发送器
 *
 * @author limaofeng
 */
@Slf4j
public class ShortMessageHandler implements MessageChannel<SmsMessage> {

  private final String provider;
  private final ShortMessageSendService shortMessageSendService;

  public ShortMessageHandler(String provider, ShortMessageSendService shortMessageSendService) {
    this.provider = provider;
    this.shortMessageSendService = shortMessageSendService;
  }

  public void send(SmsMessage message) throws MessageException {

    ShortMessageInfo info =
        shortMessageSendService.send(
            this.provider,
            message.getTemplateCode(),
            message.getTemplateParams(),
            message.getSignName(),
            message.getPhones());

    log.info("短信发送成功: {}", info);
  }

  /**
   * 转换为短信服务配置
   *
   * @param config 配置
   * @return 短信服务配置
   */
  private SMSProviderConfig toConfig(SMSChannelConfig config) {
    if (Objects.requireNonNull(config.getProvider()) == SMSProvider.ALIYUN) {
      return AliyunSMSProviderConfig.builder()
          .key(SMSProvider.ALIYUN.name() + "." + config.getAccessKeyId())
          .accessKeyId(config.getAccessKeyId())
          .accessKeySecret(config.getAccessKeySecret())
          .build();
    }
    throw new IllegalArgumentException("不支持的短信服务提供商");
  }
}
