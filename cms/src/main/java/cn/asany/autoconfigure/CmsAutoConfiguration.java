package cn.asany.autoconfigure;

import cn.asany.cms.article.graphql.input.AcceptArticleCategory;
import cn.asany.cms.body.domain.Content;
import cn.asany.cms.module.CmsModule;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 内容管理 自动配置
 *
 * @author limaofeng
 */
@Configuration
@EntityScan({
  "cn.asany.cms.body.domain",
  "cn.asany.cms.article.domain",
  "cn.asany.cms.circle.domain",
  "cn.asany.cms.learn.domain",
  "cn.asany.cms.permission.domain",
  "cn.asany.cms.special.domain",
})
@ComponentScan({
  "cn.asany.cms.*.dao",
  "cn.asany.cms.*.converter",
  "cn.asany.cms.*.service",
  "cn.asany.cms.*.listener",
  "cn.asany.cms.*.graphql"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.cms.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class CmsAutoConfiguration {

  @Bean("CMS.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder schemaDictionary() {
    return dictionary -> {
      dictionary.add("AcceptArticleCategory", AcceptArticleCategory.class);
      dictionary.add("ArticleContent", Content.class);
    };
  }

  @Bean
  public CmsModule cmsModule() {
    return new CmsModule();
  }
}
