package cn.asany.shanhai.autoconfigure;

import cn.asany.shanhai.core.support.dao.CustomClassLoaderService;
import cn.asany.shanhai.core.support.dao.ManualTransactionManager;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.ModelDelegateFactory;
import cn.asany.shanhai.core.support.graphql.execution.ModelTransactionInstrumentation;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.model.IModelFeature;
import cn.asany.shanhai.core.support.model.ModelFeatureRegistry;
import cn.asany.shanhai.data.engine.DefaultDataSourceLoader;
import cn.asany.shanhai.data.engine.IDataSourceBuilder;
import cn.asany.shanhai.data.engine.IDataSourceLoader;
import cn.asany.shanhai.data.engine.IDataSourceOptions;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.autoconfigure.GraphQLAutoConfiguration;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 自动配置
 *
 * @author limaofeng
 */
@Configuration
@AutoConfigureBefore(GraphQLAutoConfiguration.class)
@EntityScan({
  "cn.asany.shanhai.core.domain",
  "cn.asany.shanhai.data.domain",
  "cn.asany.shanhai.view.domain",
  "cn.asany.shanhai.gateway.domain",
})
@ComponentScan({
  "cn.asany.shanhai.core.support.model.types",
  "cn.asany.shanhai.core.support.model.features",
  "cn.asany.shanhai.core.convert",
  "cn.asany.shanhai.core.utils",
  "cn.asany.shanhai.core.dao",
  "cn.asany.shanhai.core.listener",
  "cn.asany.shanhai.core.service",
  "cn.asany.shanhai.core.graphql",
  "cn.asany.shanhai.view.dao",
  "cn.asany.shanhai.view.service",
  "cn.asany.shanhai.view.graphql",
  "cn.asany.shanhai.data.engine",
  "cn.asany.shanhai.data.dao",
  "cn.asany.shanhai.data.service",
  "cn.asany.shanhai.data.graphql",
  "cn.asany.shanhai.gateway.dao",
  "cn.asany.shanhai.gateway.service",
  "cn.asany.shanhai.gateway.graphql",
})
@EnableJpaRepositories(
    basePackages = {
      "cn.asany.shanhai.core.dao",
      "cn.asany.shanhai.data.dao",
      "cn.asany.shanhai.view.dao",
      "cn.asany.shanhai.gateway.dao"
    },
    repositoryBaseClass = ComplexJpaRepository.class)
@Slf4j
@Import({CustomFieldTypeConfig.class})
public class ShanhaiAutoConfiguration {

  @Bean
  public ModelDelegateFactory buildModelDelegateFactory() {
    return new ModelDelegateFactory();
  }

  @Bean
  public FieldTypeRegistry buildFieldTypeRegistry(List<FieldType<?, ?>> fieldTypes) {
    FieldTypeRegistry registry = new FieldTypeRegistry();
    fieldTypes.forEach(registry::addType);
    return registry;
  }

  @Bean
  public ModelSessionFactory buildRuntimeSessionFactory() {
    return new ModelSessionFactory();
  }

  @Bean
  public ManualTransactionManager manualTransactionManager() {
    return new ManualTransactionManager(buildRuntimeSessionFactory());
  }

  @Bean
  public ModelTransactionInstrumentation transactionInstrumentation() {
    return new ModelTransactionInstrumentation(manualTransactionManager());
  }

  @Bean
  public ModelFeatureRegistry buildModelFeatureRegistry(List<IModelFeature> features) {
    ModelFeatureRegistry registry = new ModelFeatureRegistry();
    features.forEach(registry::add);
    return registry;
  }

  @Bean
  public IDataSourceLoader dataSourceFactory(
      List<IDataSourceBuilder<IDataSourceOptions>> builders) {
    IDataSourceLoader factory = new DefaultDataSourceLoader();
    builders.forEach(value -> factory.addBuilder(value.type(), value));
    return factory;
  }

  @Bean
  public CustomClassLoaderService customClassLoaderService() {
    return new CustomClassLoaderService();
  }
}
