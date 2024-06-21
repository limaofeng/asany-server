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

import cn.asany.base.common.BatchPayload;
import cn.asany.base.common.Ownership;
import cn.asany.base.common.TicketTarget;
import cn.asany.system.domain.Dict;
import cn.asany.system.service.DictService;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.HandlebarsTemplateUtils;
import net.asany.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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

  @Bean("base.startupRunner")
  public CommandLineRunner startupRunner(@Autowired DictService dictService) {
    return args ->
        HandlebarsTemplateUtils.registerHelper(
            "dict",
            (context, options) -> {
              String type = (String) options.params[0];
              String value = context.toString();
              return dictService.findByCode(value, type).map(Dict::getName).orElse(value);
            });
  }
}
