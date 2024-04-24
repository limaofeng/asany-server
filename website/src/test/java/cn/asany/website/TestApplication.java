package cn.asany.website;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.security.auth.core.*;
import net.asany.jfantasy.framework.security.auth.core.token.ConsumerTokenServices;
import net.asany.jfantasy.framework.security.auth.oauth2.DefaultTokenServices;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AccessToken;
import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@EnableCaching
@ComponentScan(
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {GraphQLMutationResolver.class, GraphQLQueryResolver.class}))
@EnableAutoConfiguration(
    exclude = {
      MongoAutoConfiguration.class,
      QuartzAutoConfiguration.class,
      WebMvcAutoConfiguration.class,
      AuditAutoConfiguration.class,
    })
public class TestApplication {

  @Bean
  public ClientDetailsService clientDetailsService() {
    return clientId -> null;
  }

  @Bean
  public ConsumerTokenServices consumerTokenServices(
      TokenStore<OAuth2AccessToken> tokenStore, ClientDetailsService clientDetailsService) {
    return new DefaultTokenServices(tokenStore, clientDetailsService, new ThreadPoolTaskExecutor());
  }
}
