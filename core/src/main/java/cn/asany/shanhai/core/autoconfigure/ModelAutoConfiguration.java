package cn.asany.shanhai.core.autoconfigure;

import cn.asany.shanhai.core.support.FieldType;
import cn.asany.shanhai.core.support.FieldTypeRegistry;
import cn.asany.shanhai.core.support.IModelFeature;
import cn.asany.shanhai.core.support.ModelFeatureRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan({"cn.asany.shanhai.core.support.types", "cn.asany.shanhai.core.support.features", "cn.asany.shanhai.core.runners"})
public class ModelAutoConfiguration {

    @Bean
    public FieldTypeRegistry buildFieldTypeRegistry(List<FieldType> fieldTypes) {
        FieldTypeRegistry registry = new FieldTypeRegistry();
        fieldTypes.stream().forEach(item -> registry.addType(item));
        return registry;
    }

    @Bean
    public ModelFeatureRegistry buildModelFeatureRegistry(List<IModelFeature> features) {
        ModelFeatureRegistry registry = new ModelFeatureRegistry();
        features.stream().forEach(item -> registry.add(item));
        return registry;
    }

}
