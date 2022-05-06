package cn.asany.autoconfigure;

import cn.asany.cardhop.contacts.service.DefaultContactsServiceFactory;
import cn.asany.cardhop.integration.IContactsService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Slf4j
@EntityScan({"cn.asany.cardhop.contacts.bean"})
@ComponentScan({
  "cn.asany.cardhop.contacts.dao",
  "cn.asany.cardhop.contacts.convert",
  "cn.asany.cardhop.contacts.service",
  "cn.asany.cardhop.contacts.graphql",
  "cn.asany.cardhop.integration.convert",
  "cn.asany.cardhop.integration.service",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.cardhop.contacts.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class CardhopAutoConfiguration {

  @Bean
  public DefaultContactsServiceFactory contactsServiceFactory(List<IContactsService> services) {
    return new DefaultContactsServiceFactory(services);
  }
}
