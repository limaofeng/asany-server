package cn.asany.autoconfigure;

import cn.asany.ui.library.graphql.type.ILibrary;
import cn.asany.ui.library.graphql.type.IconLibrary;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** @author limaofeng */
@Configuration
@EntityScan("cn.asany.ui*.domain")
@ComponentScan({
  "cn.asany.ui.*.dao",
  "cn.asany.ui.*.convert",
  "cn.asany.ui.*.service",
  "cn.asany.ui.*.graphql"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.ui.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class UIAutoConfiguration {

  @Bean
  public SchemaParserDictionaryBuilder uiSchemaParserDictionaryBuilder() {
    return dictionary -> {
      dictionary.add("Library", ILibrary.class);
      dictionary.add("IconLibrary", IconLibrary.class);
    };
  }
}
