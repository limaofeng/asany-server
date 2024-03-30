package cn.asany.autoconfigure;

import cn.asany.base.common.TicketTarget;
import cn.asany.base.common.TicketTargetBuilder;
import cn.asany.base.common.TicketTargetResolver;
import cn.asany.crm.support.component.DefaultTicketTargetResolver;
import java.util.List;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"cn.asany.crm.*.domain"})
@ComponentScan({
  "cn.asany.crm.*.dao",
  "cn.asany.crm.*.graphql",
  "cn.asany.crm.*.service",
  "cn.asany.crm.*.convert"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.crm.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class CustomerRelationshipManagementAutoConfiguration {

  @Bean
  public TicketTargetResolver ticketTargetResolver(
      List<TicketTargetBuilder<? extends TicketTarget>> builders) {
    return new DefaultTicketTargetResolver(builders);
  }
}
