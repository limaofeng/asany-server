package cn.asany.autoconfigure;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.core.DefaultStorageResolver;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.minio.MinIOStorageConfig;
import cn.asany.storage.core.engine.oss.OSSStorageConfig;
import cn.asany.storage.data.graphql.directive.FileObjectFormatDirective;
import cn.asany.storage.data.graphql.scalar.FileObjectCoercing;
import cn.asany.storage.data.web.FileFilter;
import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.kickstart.tools.boot.SchemaDirective;
import graphql.schema.GraphQLScalarType;
import java.util.List;
import javax.servlet.DispatcherType;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** @author limaofeng */
@Configuration
@EntityScan("cn.asany.storage.data.bean")
@ComponentScan({
  "cn.asany.storage.core",
  "cn.asany.storage.*.dao",
  "cn.asany.storage.*.service",
  "cn.asany.storage.*.runner",
  "cn.asany.storage.*.converter",
  "cn.asany.storage.*.graphql",
  "cn.asany.storage.*.rest"
})
public class StorageAutoConfiguration {

  @Bean
  public GraphQLScalarType uploadScalarDefine() {
    return ApolloScalars.Upload;
  }

  @Bean
  public FileObjectCoercing fileObjectCoercing() {
    return new FileObjectCoercing();
  }

  @Bean
  public GraphQLScalarType fileByScalar() {
    return GraphQLScalarType.newScalar()
        .name("FileObject")
        .description("文件对象")
        .coercing(fileObjectCoercing())
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
  public SchemaDirective fileObjectFormatDirective() {
    return new SchemaDirective("fileObjectFormat", new FileObjectFormatDirective());
  }

  @Bean
  public FileFilter fileFilter() {
    return new FileFilter();
  }

  @Bean
  public FilterRegistrationBean fileFilterRegistrationBean() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(fileFilter());
    filterRegistrationBean.setEnabled(true);
    filterRegistrationBean.setOrder(300);
    filterRegistrationBean.addInitParameter("targetFilterLifecycle", "true");
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
    filterRegistrationBean.addUrlPatterns("/*");
    return filterRegistrationBean;
  }
}
