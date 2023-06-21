package cn.asany.autoconfigure;

import cn.asany.base.sms.SMSProvider;
import cn.asany.base.sms.SMSProviderConfig;
import cn.asany.message.api.MessageSenderBuilder;
import cn.asany.message.api.SMSSenderConfig;
import cn.asany.sms.provider.AliyunSMSProviderConfig;
import cn.asany.sms.provider.ShortMessageServiceProviderFactory;
import java.util.Objects;

/**
 * 短信发送器构建器
 *
 * @author limaofeng
 */
public class ShortMessageSenderBuilder
    implements MessageSenderBuilder<ShortMessageSender, SMSSenderConfig> {

  private final ShortMessageServiceProviderFactory providerFactory;

  public ShortMessageSenderBuilder(ShortMessageServiceProviderFactory providerFactory) {
    this.providerFactory = providerFactory;
  }

  @Override
  public boolean supports(Class<SMSSenderConfig> clazz) {
    return SMSSenderConfig.class.isAssignableFrom(clazz);
  }

  @Override
  public ShortMessageSender build(SMSSenderConfig config) {
    SMSProviderConfig smsProviderConfig = toConfig(config);
    return new ShortMessageSender(providerFactory.createProvider(smsProviderConfig));
  }

  /**
   * 转换为短信服务配置
   *
   * @param config 配置
   * @return 短信服务配置
   */
  private SMSProviderConfig toConfig(SMSSenderConfig config) {
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
