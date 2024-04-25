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

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.Storage;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.core.DefaultStorageResolver;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.minio.MinIOStorageConfig;
import cn.asany.storage.core.engine.oss.OSSStorageConfig;
import cn.asany.storage.data.graphql.directive.FileFormatDirective;
import cn.asany.storage.data.graphql.scalar.FileCoercing;
import cn.asany.storage.data.service.AuthTokenService;
import cn.asany.storage.data.service.StorageService;
import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.schema.GraphQLScalarType;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 存储自动配置
 *
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.storage.data.domain")
@ComponentScan({
  "cn.asany.storage.core",
  "cn.asany.storage.plugin",
  "cn.asany.storage.*.dao",
  "cn.asany.storage.*.service",
  "cn.asany.storage.*.converter",
  "cn.asany.storage.*.convert",
  "cn.asany.storage.*.graphql",
  "cn.asany.storage.*.rest",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.storage.*.dao",
    repositoryBaseClass = SimpleAnyJpaRepository.class)
public class StorageAutoConfiguration {

  @Bean
  public GraphQLScalarType uploadScalarDefine() {
    return ApolloScalars.Upload;
  }

  @Bean
  public FileCoercing fileCoercing(@Autowired Environment environment) {
    return new FileCoercing(environment);
  }

  @Bean
  public GraphQLScalarType fileByScalar(@Autowired Environment environment) {
    return GraphQLScalarType.newScalar()
        .name("File")
        .description("文件")
        .coercing(fileCoercing(environment))
        .build();
  }

  @Bean
  public StorageResolver storageResolver(
      StorageService storageService,
      List<StorageBuilder<? extends Storage, ? extends IStorageConfig>> builders) {
    return new DefaultStorageResolver(storageService, builders);
  }

  @Bean
  public SchemaParserDictionaryBuilder storageSchemaParserDictionaryBuilder() {
    return dictionary -> {
      dictionary.add("StorageProperties", IStorageConfig.class);
      dictionary.add("MinIOProperties", MinIOStorageConfig.class);
      dictionary.add("OSSProperties", OSSStorageConfig.class);
    };
  }

  @Bean
  public SchemaDirective fileFormatDirective(
      @Autowired AuthTokenService authTokenService, StorageResolver storageResolver) {
    return new SchemaDirective(
        "fileFormat", new FileFormatDirective(authTokenService, storageResolver));
  }
}
