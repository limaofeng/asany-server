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

import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 工单 配置
 *
 * @author limaofeng
 */
@Configuration
@EntityScan({
  "cn.asany.pm.project.domain",
  "cn.asany.pm.issue.*.domain",
})
@ComponentScan({
  "cn.asany.pm.project.dao",
  "cn.asany.pm.project.service",
  "cn.asany.pm.project.convert",
  "cn.asany.pm.project.graphql",
  "cn.asany.pm.issue.*.dao",
  "cn.asany.pm.issue.*.service",
  "cn.asany.pm.issue.*.convert",
  "cn.asany.pm.issue.*.graphql",
})
@EnableJpaRepositories(
    basePackages = {"cn.asany.pm.project.dao", "cn.asany.pm.issue.*.dao"},
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class ProjectManagementAutoConfiguration {

  @Bean("PM.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder schemaDictionary() {
    return dictionary -> {};
  }
}
