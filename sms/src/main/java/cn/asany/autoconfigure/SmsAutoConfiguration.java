package cn.asany.autoconfigure;

import cn.asany.autoconfigure.properties.AliyunProvider;
import cn.asany.autoconfigure.properties.SmsProperties;
import cn.asany.autoconfigure.properties.SmsProvider;
import cn.asany.base.sms.ShortMessageSendService;
import cn.asany.sms.service.AliyunShortMessageSendService;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 短信服务自动装配
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.sms.domain"})
@ComponentScan({
  "cn.asany.sms.graphql",
  "cn.asany.sms.dao",
  "cn.asany.sms.service",
  "cn.asany.sms.graphql",
  "cn.asany.sms.listener"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.sms.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
@EnableConfigurationProperties({SmsProperties.class})
public class SmsAutoConfiguration {

  /**
   * 使用AK&SK初始化账号Client
   *
   * @param properties 配置
   * @return Client
   */
  @Bean("aliyun.sms.client")
  public Client createClient(SmsProperties properties) throws Exception {
    SmsProvider providers = properties.getProvider();
    AliyunProvider provider = providers.getAliyun();
    String accessKeyId = provider.getAccessKeyId();
    String accessKeySecret = provider.getAccessKeySecret();
    Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
    // 访问的域名
    config.endpoint = "dysmsapi.aliyuncs.com";
    return new com.aliyun.dysmsapi20170525.Client(config);
  }

  @Bean
  public ShortMessageSendService shortMessageSendService(@Autowired  Client client) {
    return new AliyunShortMessageSendService(client);
  }
}
