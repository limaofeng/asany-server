package cn.asany.shanhai;

import cn.asany.autoconfigure.EmailAutoConfiguration;
import cn.asany.autoconfigure.ImapServerAutoConfiguration;
import cn.asany.autoconfigure.POP3ServerAutoConfiguration;
import cn.asany.autoconfigure.SmtpServerAutoConfiguration;
import cn.asany.shanhai.autoconfigure.ModelAutoConfiguration;
import cn.asany.shanhai.autoconfigure.ShanhaiAutoConfiguration;
import graphql.kickstart.autoconfigure.tools.GraphQLJavaToolsAutoConfiguration;
import graphql.kickstart.autoconfigure.web.servlet.GraphQLWebAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.autoconfigure.GraphQLAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author limaofeng
 * @version V1.0 @Description: 测试程序入口
 * @date 2022/7/28 9:12 9:12
 */
@Slf4j
@Configuration
@EnableCaching
@EnableAutoConfiguration(
    exclude = {
      MongoAutoConfiguration.class,
      QuartzAutoConfiguration.class,
      WebMvcAutoConfiguration.class,
      AuditAutoConfiguration.class,
      ModelAutoConfiguration.class,
      ShanhaiAutoConfiguration.class,
      GraphQLAutoConfiguration.class,
      GraphQLWebAutoConfiguration.class,
      GraphQLJavaToolsAutoConfiguration.class,
      EmailAutoConfiguration.class,
      SmtpServerAutoConfiguration.class,
      ImapServerAutoConfiguration.class,
      POP3ServerAutoConfiguration.class,
      RedisRepositoriesAutoConfiguration.class,
      ElasticsearchRepositoriesAutoConfiguration.class
    })
public class TestApplication {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
