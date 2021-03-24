package cn.asany.shanhai.core.autoconfigure;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.GraphQLServer;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.model.IModelFeature;
import cn.asany.shanhai.core.support.model.ModelFeatureRegistry;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import graphql.GraphQL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ModelService modelService;

    private HibernateMappingHelper hibernateMappingHelper;

    private ModelSessionFactory modelSessionFactory;

    private GraphQLServer graphQLServer;

    @Bean
    public GraphQLServer buildGraphQLServer() {
        return this.graphQLServer = new GraphQLServer();
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

    public void load() {
        List<Model> types = modelService.findAll(ModelType.SCALAR, ModelType.TYPE, ModelType.INPUT);
        List<Model> models = modelService.findAll(ModelType.OBJECT);

        graphQLServer.setTypes(types);

        for (Model model : models) {
            ModelRepository repository = modelSessionFactory.buildModelRepository(model);

            graphQLServer.addModel(model, repository);
        }

        GraphQL graphQL = graphQLServer.buildServer();

        log.debug("Scheme:" + graphQL);

        modelSessionFactory.update();


    }
}
