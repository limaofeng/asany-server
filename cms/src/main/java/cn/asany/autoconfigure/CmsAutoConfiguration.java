package cn.asany.autoconfigure;

import cn.asany.cms.article.bean.HtmlContent;
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
  "cn.asany.cms.article.bean",
  "cn.asany.cms.circle.bean",
  "cn.asany.cms.learn.bean",
  "cn.asany.cms.permission.bean",
  "cn.asany.cms.special.bean",
})
@ComponentScan({
  "cn.asany.cms.*.dao",
  "cn.asany.cms.*.converter",
  "cn.asany.cms.*.service",
  "cn.asany.cms.*.graphql"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.cms.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class CmsAutoConfiguration {

  @Bean("CMS.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder schemaDictionary() {
    return dictionary -> {
      dictionary.add("HtmlContent", HtmlContent.class);
    };
  }

  @Bean
  public CmsModule cmsModule() {
    return new CmsModule();
  }
}
