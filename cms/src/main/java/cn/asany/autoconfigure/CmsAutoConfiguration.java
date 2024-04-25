/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.framework.search.config.IndexedScan;
import net.asany.jfantasy.graphql.SchemaParserDictionaryBuilder;
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
    repositoryBaseClass = SimpleAnyJpaRepository.class)
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
