package cn.asany.autoconfigure;

import cn.asany.base.sms.SMSProvider;
import cn.asany.base.sms.SMSProviderConfig;
import cn.asany.base.sms.ShortMessageSendService;
import cn.asany.message.api.MessageChannelBuilder;
import cn.asany.message.api.SMSChannelConfig;
import cn.asany.sms.provider.AliyunSMSProviderConfig;
import java.util.Objects;

/**
 * 短信发送器构建器
 *
 * @author limaofeng
 */
public class ShortMessageChannelBuilder
    implements MessageChannelBuilder<ShortMessageHandler, SMSChannelConfig> {

  private final ShortMessageSendService shortMessageSendService;

  public ShortMessageChannelBuilder(ShortMessageSendService shortMessageSendService) {
    this.shortMessageSendService = shortMessageSendService;
  }

  @Override
  public boolean supports(Class<SMSChannelConfig> clazz) {
    return SMSChannelConfig.class.isAssignableFrom(clazz);
  }

  @Override
  public ShortMessageHandler build(SMSChannelConfig config) {
    SMSProviderConfig smsProviderConfig = toConfig(config);
    return new ShortMessageHandler("", shortMessageSendService);
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
