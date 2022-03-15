package cn.asany.autoconfigure;

import cn.asany.cardhop.contacts.service.DefaultContactsServiceFactory;
import cn.asany.cardhop.integration.IContactsService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@EntityScan({"cn.asany.cardhop.*.*.bean"})
@ComponentScan({
  "cn.asany.cardhop.*.dao",
  "cn.asany.cardhop.*.convert",
  "cn.asany.cardhop.*.service",
  "cn.asany.cardhop.*.graphql",
})
public class CardhopAutoConfiguration {

  @Bean
  public DefaultContactsServiceFactory contactsServiceFactory(List<IContactsService> services) {
    return new DefaultContactsServiceFactory(services);
  }
}
