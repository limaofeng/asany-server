package cn.asany.shanhai;

import cn.asany.autoconfigure.EmailAutoConfiguration;
import cn.asany.autoconfigure.ImapServerAutoConfiguration;
import cn.asany.autoconfigure.SmtpServerAutoConfiguration;
import graphql.kickstart.autoconfigure.tools.GraphQLJavaToolsAutoConfiguration;
import graphql.kickstart.autoconfigure.web.servlet.GraphQLWebAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.autoconfigure.GraphQLAutoConfiguration;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

/**
 * @author limaofeng
 * @version V1.0 @Description: 测试程序入口
 * @date 2019/2/13 4:04 PM
 */
@Slf4j
@Configuration
@ComponentScan({"cn.asany.openapi.service", "cn.asany.*.*.convert"})
@EntityScan({
  "cn.asany.*.*.bean",
})
@EnableCaching
@EnableJpaRepositories(
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          value = {JpaRepository.class})
    },
    basePackages = {
      "cn.asany.*.dao",
      "cn.asany.openapi.dao",
    },
    repositoryBaseClass = ComplexJpaRepository.class)
@EnableAutoConfiguration(
    exclude = {
      MongoAutoConfiguration.class,
      QuartzAutoConfiguration.class,
      WebMvcAutoConfiguration.class,
      AuditAutoConfiguration.class,
      GraphQLAutoConfiguration.class,
      GraphQLWebAutoConfiguration.class,
      GraphQLJavaToolsAutoConfiguration.class,
      EmailAutoConfiguration.class,
      SmtpServerAutoConfiguration.class,
      ImapServerAutoConfiguration.class
    })
public class TestApplication {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
