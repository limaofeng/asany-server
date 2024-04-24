package cn.asany.im;

import graphql.kickstart.autoconfigure.tools.GraphQLJavaToolsAutoConfiguration;
import graphql.kickstart.autoconfigure.web.servlet.GraphQLWebAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.autoconfigure.GraphQLAutoConfiguration;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.framework.security.auth.core.ClientDetails;
import net.asany.jfantasy.framework.security.auth.core.ClientDetailsService;
import net.asany.jfantasy.framework.security.auth.core.ClientRegistrationException;
import net.asany.jfantasy.framework.security.auth.core.TokenStore;
import net.asany.jfantasy.framework.security.auth.oauth2.DefaultTokenServices;
import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 测试程序入口
 *
 * @author limaofeng
 * @date 2022/7/28 9:12 9:12
 */
@Slf4j
@Configuration
@EnableJpaRepositories(
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          value = {JpaRepository.class})
    },
    repositoryBaseClass = SimpleAnyJpaRepository.class)
@EnableAutoConfiguration(
    exclude = {
      MongoAutoConfiguration.class,
      QuartzAutoConfiguration.class,
      WebMvcAutoConfiguration.class,
      AuditAutoConfiguration.class,
      GraphQLAutoConfiguration.class,
      GraphQLWebAutoConfiguration.class,
      GraphQLJavaToolsAutoConfiguration.class,
    })
public class TestApplication {

  @Bean
  public DefaultTokenServices consumerTokenServices(
      TokenStore tokenStore, ClientDetailsService clientDetailsService) {
    return new DefaultTokenServices(tokenStore, clientDetailsService, new ThreadPoolTaskExecutor());
  }

  @Bean
  public ClientDetailsService clientDetailsService() {
    return new ClientDetailsService() {
      @Override
      public ClientDetails loadClientByClientId(String clientId)
          throws ClientRegistrationException {
        return null;
      }
    };
  }
}
