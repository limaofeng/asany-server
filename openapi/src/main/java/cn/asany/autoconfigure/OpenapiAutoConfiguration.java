package cn.asany.autoconfigure;

import cn.asany.openapi.configs.WeixinConfig;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 开发平台 自动配置
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.openapi.domain"})
@ComponentScan({
  "cn.asany.openapi.graphql",
  "cn.asany.openapi.dao",
  "cn.asany.openapi.service",
  "cn.asany.openapi.graphql",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.openapi.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class OpenapiAutoConfiguration {

  @Bean
  public SchemaParserDictionaryBuilder openapiSchemaParserDictionaryBuilder() {
    return dictionary -> dictionary.add("Weixin", WeixinConfig.class);
  }
}
