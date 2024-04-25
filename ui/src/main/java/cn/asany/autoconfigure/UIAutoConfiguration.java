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

import cn.asany.ui.library.graphql.type.ILibrary;
import cn.asany.ui.library.graphql.type.IconLibrary;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.ui.*.domain")
@ComponentScan({
  "cn.asany.ui.*.dao",
  "cn.asany.ui.*.convert",
  "cn.asany.ui.*.service",
  "cn.asany.ui.*.graphql"
})
@EnableJpaRepositories(
    basePackages = "cn.asany.ui.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class UIAutoConfiguration {

  @Bean
  public SchemaParserDictionaryBuilder uiSchemaParserDictionaryBuilder() {
    return dictionary -> {
      dictionary.add("Library", ILibrary.class);
      dictionary.add("IconLibrary", IconLibrary.class);
    };
  }
}
