package cn.asany.autoconfigure;

import cn.asany.ui.library.graphql.type.ILibrary;
import cn.asany.ui.library.graphql.type.IconLibrary;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** @author limaofeng */
@Configuration
@EntityScan("cn.asany.ui.*.bean")
@ComponentScan({"cn.asany.ui.*.service", "cn.asany.ui.*.graphql"})
public class UIAutoConfiguration {

  @Bean
  public SchemaParserDictionaryBuilder uiSchemaParserDictionaryBuilder() {
    return dictionary -> {
      dictionary.add("Library", ILibrary.class);
      dictionary.add("IconLibrary", IconLibrary.class);
    };
  }
}
