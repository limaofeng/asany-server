package cn.asany.autoconfigure;

import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 工单 配置
 *
 * @author limaofeng
 */
@Configuration
@EntityScan({
  "cn.asany.pm.project.domain",
  "cn.asany.pm.issue.*.domain",
})
@ComponentScan({
  "cn.asany.pm.project.dao",
  "cn.asany.pm.project.service",
  "cn.asany.pm.project.convert",
  "cn.asany.pm.project.graphql",
  "cn.asany.pm.issue.*.dao",
  "cn.asany.pm.issue.*.service",
  "cn.asany.pm.issue.*.convert",
  "cn.asany.pm.issue.*.graphql",
})
@EnableJpaRepositories(
    basePackages = {"cn.asany.pm.project.dao", "cn.asany.pm.issue.*.dao"},
    repositoryBaseClass = ComplexJpaRepository.class)
public class ProjectManagementAutoConfiguration {

  @Bean("PM.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder schemaDictionary() {
    return dictionary -> {};
  }
}
