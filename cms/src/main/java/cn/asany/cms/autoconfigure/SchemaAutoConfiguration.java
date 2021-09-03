package cn.asany.cms.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2020/3/2 4:03 下午
 */
@Configuration
public class SchemaAutoConfiguration {
  @Bean
  public SchemaDictionary schemaDictionary() {
    return new SchemaDictionary();
  }
}
