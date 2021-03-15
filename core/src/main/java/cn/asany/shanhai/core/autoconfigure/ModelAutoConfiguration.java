package cn.asany.shanhai.core.autoconfigure;

import cn.asany.shanhai.core.support.*;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan({
    "cn.asany.shanhai.core.support.types",
    "cn.asany.shanhai.core.support.features",
    "cn.asany.shanhai.core.runners",
    "cn.asany.shanhai.core.service",
    "cn.asany.shanhai.core.utils",
    "cn.asany.shanhai.core.dao"
})
public class ModelAutoConfiguration {

    @Bean
    public FieldTypeRegistry buildFieldTypeRegistry(List<FieldType> fieldTypes) {
        FieldTypeRegistry registry = new FieldTypeRegistry();
        fieldTypes.stream().forEach(item -> registry.addType(item));
        return registry;
    }

    @Bean
    public RuntimeMetadataRegistry buildRuntimeMetadataRegistry() {
        RuntimeMetadataRegistry registry = new RuntimeMetadataRegistry();
        return registry;
    }

    @Bean
    public RuntimeJpaRepositoryFactory buildRuntimeJpaRepositoryFactory() {
        RuntimeJpaRepositoryFactory registry = new RuntimeJpaRepositoryFactory();
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
