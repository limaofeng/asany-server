package cn.asany.shanhai.autoconfigure;

import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.GraphQLServer;
import cn.asany.shanhai.core.support.graphql.ModelDelegateFactory;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.model.IModelFeature;
import cn.asany.shanhai.core.support.model.ModelFeatureRegistry;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ModelAutoConfiguration {

    private HibernateMappingHelper hibernateMappingHelper;

    private ModelSessionFactory modelSessionFactory;

    private GraphQLServer graphQLServer;

    @Bean
    public GraphQLServer buildGraphQLServer() {
        return this.graphQLServer = new GraphQLServer();
    }

    @Bean
    public ModelDelegateFactory buildModelDelegateFactory() {
        return new ModelDelegateFactory();
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
        return this.modelSessionFactory = sessionFactory;
    }

    @Bean
    public ModelFeatureRegistry buildModelFeatureRegistry(List<IModelFeature> features) {
        ModelFeatureRegistry registry = new ModelFeatureRegistry();
        features.stream().forEach(item -> registry.add(item));
        return registry;
    }

    public String fromNow(long time) {
        long times = System.currentTimeMillis() - time;
        if (times < 1000) {
            return times + "ms";
        }
        times = times / 1000;
        return times + "s";
    }

}
