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
public class ShortMessageHandler implements MessageChannel {

  private final String provider;
  private final ShortMessageSendService shortMessageSendService;

  public ShortMessageHandler(String provider, ShortMessageSendService shortMessageSendService) {
    this.provider = provider;
    this.shortMessageSendService = shortMessageSendService;
  }

  public void send(Message message) throws MessageException {
    SmsMessage smsMessage = (SmsMessage) message;

    ShortMessageInfo info =
        shortMessageSendService.send(
            this.provider,
            smsMessage.getTemplateCode(),
            smsMessage.getTemplateParams(),
            smsMessage.getSignName(),
            smsMessage.getPhones());

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
