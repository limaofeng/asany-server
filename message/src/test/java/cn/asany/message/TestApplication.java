package cn.asany.message;

import cn.asany.autoconfigure.AsanySecurityAutoConfiguration;
import cn.asany.autoconfigure.OrganizationAutoConfiguration;
import graphql.kickstart.autoconfigure.tools.GraphQLJavaToolsAutoConfiguration;
import graphql.kickstart.autoconfigure.web.servlet.GraphQLWebAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.autoconfigure.GraphQLAutoConfiguration;
import net.asany.jfantasy.autoconfigure.ResourceServerAutoConfiguration;
import net.asany.jfantasy.framework.security.auth.core.TokenStore;
import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import net.asany.jfantasy.framework.security.auth.oauth2.DefaultTokenServices;
import net.asany.jfantasy.framework.security.auth.core.ClientDetailsService;

@Slf4j
@Configuration
@ComponentScan({"cn.asany.message.*.service"})
@EntityScan({
  "cn.asany.*.*.domain",
})
@EnableCaching
@EnableJpaRepositories(
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          value = {JpaRepository.class})
    },
    basePackages = {
      "cn.asany.message.*.dao",
    },
    repositoryBaseClass = SimpleAnyJpaRepository.class)
@Import({
  AsanySecurityAutoConfiguration.class,
  OrganizationAutoConfiguration.class,
  ResourceServerAutoConfiguration.class
})
@EnableAutoConfiguration(
    exclude = {
      MongoAutoConfiguration.class,
      QuartzAutoConfiguration.class,
      AuditAutoConfiguration.class,
      GraphQLAutoConfiguration.class,
      GraphQLWebAutoConfiguration.class,
      GraphQLJavaToolsAutoConfiguration.class,
    })
public class TestApplication {

  @Bean
  public TestClientDetailsService testClientDetailsService() {
    return new TestClientDetailsService();
  }
}
