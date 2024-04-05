package cn.asany.autoconfigure;

import cn.asany.base.common.BatchPayload;
import cn.asany.base.common.Ownership;
import cn.asany.base.common.TicketTarget;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 基础模块 自动配置
 *
 * @author limaofeng
 */
@Configuration
@Slf4j
@EntityScan({"cn.asany.base.*.domain"})
@ComponentScan({
  "cn.asany.base.*.service",
  "cn.asany.base.*.graphql",
})
public class BaseAutoConfiguration {

  @Bean("base.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder schemaDictionary() {
    return dictionary -> {
      dictionary.add("Owner", Ownership.class);
      dictionary.add("TicketTarget", TicketTarget.class);
      dictionary.add("BatchPayload", BatchPayload.class);
    };
  }
}
