package cn.asany.autoconfigure;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.article.graphql.input.AcceptArticleCategory;
import cn.asany.cms.content.domain.DocumentContent;
import cn.asany.cms.content.domain.ImageContent;
import cn.asany.cms.content.domain.TextContent;
import cn.asany.cms.content.domain.VideoContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.cms.content.service.ArticleContentHandler;
import cn.asany.cms.content.service.ArticleContentService;
import cn.asany.cms.module.CmsApplicationModule;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.search.config.IndexedScan;
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
@IndexedScan("cn.asany.cms.*.domain")
@EntityScan({
  "cn.asany.cms.*.domain",
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
      dictionary.add("ContentType", ContentType.class);
      dictionary.add("TextContent", TextContent.class);
      dictionary.add("VideoContent", VideoContent.class);
      dictionary.add("ImageContent", ImageContent.class);
      dictionary.add("DocumentContent", DocumentContent.class);
    };
  }

  @Bean
  public ArticleContentService articleContentService(List<ArticleContentHandler<?>> handlers) {
    return new ArticleContentService(
        handlers.stream()
            .map(item -> (ArticleContentHandler<ArticleContent>) item)
            .collect(Collectors.toList()));
  }

  @Bean
  public CmsApplicationModule cmsModule() {
    return new CmsApplicationModule();
  }
}
