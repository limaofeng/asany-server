package cn.asany.autoconfigure;

import cn.asany.autoconfigure.properties.SmsProperties;
import cn.asany.sms.provider.ShortMessageServiceProviderFactory;
import cn.asany.sms.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
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

  @Bean
  public ShortMessageServiceProviderFactory shortMessageServiceProviderFactory(
      MessageService messageService) {
    return new ShortMessageServiceProviderFactory(messageService);
  }
}
