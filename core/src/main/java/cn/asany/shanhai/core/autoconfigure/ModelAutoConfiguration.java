package cn.asany.shanhai.core.autoconfigure;

import cn.asany.shanhai.core.support.dao.ModelJpaRepositoryFactory;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.ModelGraphQLSchemaFactory;
import cn.asany.shanhai.core.support.model.*;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan({
    "cn.asany.shanhai.core.support.model.types",
    "cn.asany.shanhai.core.support.model.features",
    "cn.asany.shanhai.core.runners",
    "cn.asany.shanhai.core.service",
    "cn.asany.shanhai.core.utils",
    "cn.asany.shanhai.core.rest",
    "cn.asany.shanhai.core.dao"
})
public class ModelAutoConfiguration {

    @Bean
    public ModelGraphQLSchemaFactory buildRuntimeGraphQLSchemaFactory() {
        return new ModelGraphQLSchemaFactory();
    }

    @Bean
    public FieldTypeRegistry buildFieldTypeRegistry(List<FieldType> fieldTypes) {
        FieldTypeRegistry registry = new FieldTypeRegistry();
        fieldTypes.stream().forEach(item -> registry.addType(item));
        return registry;
    }

    @Bean
    public ModelSessionFactory buildRuntimeSessionFactory() {
        ModelSessionFactory sessionFactory = new ModelSessionFactory();
        return sessionFactory;
    }

    @Bean
    public ModelJpaRepositoryFactory buildRuntimeJpaRepositoryFactory() {
        ModelJpaRepositoryFactory registry = new ModelJpaRepositoryFactory();
        return registry;
    }

    @Bean
    public ModelFeatureRegistry buildModelFeatureRegistry(List<IModelFeature> features) {
        ModelFeatureRegistry registry = new ModelFeatureRegistry();
        features.stream().forEach(item -> registry.add(item));
        return registry;
    }

    @Bean
    public HibernateMappingHelper buildHibernateMappingHelper() {
        return new HibernateMappingHelper();
    }

}
