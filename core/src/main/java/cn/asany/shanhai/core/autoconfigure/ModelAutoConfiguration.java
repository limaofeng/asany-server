package cn.asany.shanhai.core.autoconfigure;

import cn.asany.shanhai.core.support.FieldType;
import cn.asany.shanhai.core.support.FieldTypeRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan({"cn.asany.shanhai.core.support.types"})
public class ModelAutoConfiguration {

    @Bean
    public FieldTypeRegistry buildFieldTypeRegistry(List<FieldType> fieldTypes) {
        FieldTypeRegistry registry = new FieldTypeRegistry();
        fieldTypes.stream().forEach(item -> registry.addType(item));
        return registry;
    }

}
