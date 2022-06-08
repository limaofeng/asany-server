package cn.asany.autoconfigure;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.core.DefaultStorageResolver;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.minio.MinIOStorageConfig;
import cn.asany.storage.core.engine.oss.OSSStorageConfig;
import cn.asany.storage.data.graphql.directive.FileFormatDirective;
import cn.asany.storage.data.graphql.scalar.FileCoercing;
import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.schema.GraphQLScalarType;
import java.util.List;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** @author limaofeng */
@Configuration
@EntityScan("cn.asany.storage.data.domain")
@ComponentScan({
  "cn.asany.storage.core",
  "cn.asany.storage.plugin",
  "cn.asany.storage.*.dao",
  "cn.asany.storage.*.service",
  "cn.asany.storage.*.runner",
  "cn.asany.storage.*.converter",
  "cn.asany.storage.*.convert",
  "cn.asany.storage.*.graphql",
  "cn.asany.storage.*.rest",
})
@EnableJpaRepositories(
    basePackages = "cn.asany.storage.*.dao",
    repositoryBaseClass = ComplexJpaRepository.class)
public class StorageAutoConfiguration {

  @Bean
  public GraphQLScalarType uploadScalarDefine() {
    return ApolloScalars.Upload;
  }

  @Bean
  public FileCoercing fileCoercing() {
    return new FileCoercing();
  }

  @Bean
  public GraphQLScalarType fileByScalar() {
    return GraphQLScalarType.newScalar()
        .name("File")
        .description("文件")
        .coercing(fileCoercing())
        .build();
  }

  @Bean
  public StorageResolver storageResolver(List<StorageBuilder> builders) {
    return new DefaultStorageResolver(builders);
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
  public SchemaDirective fileFormatDirective() {
    return new SchemaDirective("fileFormat", new FileFormatDirective());
  }
}
