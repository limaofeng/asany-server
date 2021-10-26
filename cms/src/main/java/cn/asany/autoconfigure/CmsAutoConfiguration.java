package cn.asany.autoconfigure;

import cn.asany.cms.article.bean.HtmlContent;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 内容管理 自动配置
 *
 * @author limaofeng
 * @date 2020/3/2 4:03 下午
 */
@Configuration
@ComponentScan({
  "cn.asany.cms.*.dao",
})
public class CmsAutoConfiguration {
  @Bean("CMS.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder schemaDictionary() {
    return dictionary -> {
      dictionary.add("HtmlContent", HtmlContent.class);
    };
  }
}
