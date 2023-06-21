package cn.asany.sms.provider;

import cn.asany.base.sms.SMSProvider;
import cn.asany.base.sms.SMSProviderConfig;
import cn.asany.base.sms.ShortMessageServiceProvider;
import cn.asany.sms.service.MessageService;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;

/**
 * 短信服务提供商工厂
 *
 * @author limaofeng
 */
public class ShortMessageServiceProviderFactory {

  private final Map<String, ShortMessageServiceProvider> cache = new HashMap<>();

  private final MessageService messageService;

  public ShortMessageServiceProviderFactory(MessageService messageService) {
    this.messageService = messageService;
  }

  @SneakyThrows(Exception.class)
  public ShortMessageServiceProvider createProvider(SMSProviderConfig config) {
    SMSProvider provider = config.getProvider();
    if (provider == SMSProvider.ALIYUN) {
      ShortMessageServiceProvider shortMessageServiceProvider =
          new AliyunShortMessageServiceProvider();
      shortMessageServiceProvider.configure(config);
      cache.put(provider.name(), shortMessageServiceProvider);
      return shortMessageServiceProvider;
    } else {
      throw new IllegalArgumentException("Unsupported SMS provider: " + provider);
    }
  }

  public ShortMessageServiceProvider getProvider(String provider) {
    SMSProviderConfig config = messageService.getProviderConfig(provider);
    if (cache.containsKey(provider)) {
      return cache.get(provider);
    }
    return createProvider(config);
  }
}
