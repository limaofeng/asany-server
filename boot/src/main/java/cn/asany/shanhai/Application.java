package cn.asany.shanhai;

import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author limaofeng
 * @version V1.0 @Description: 应用程序入口
 * @date 2019/2/13 4:04 PM
 */
@Configuration
@ComponentScan({"cn.asany.*.*.service", "cn.asany.*.*.converter", "cn.asany.*.*.graphql"})
@EntityScan({
  "cn.asany.*.*.bean",
})
@EnableJpaRepositories(
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          value = {JpaRepository.class})
    },
    basePackages = {
      "cn.asany.*.*.dao",
    },
    repositoryBaseClass = ComplexJpaRepository.class)
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, QuartzAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(Application.class);
  }
}
