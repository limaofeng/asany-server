package cn.asany.shanhai.gateway.graphql.resolvers;

import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.gateway.bean.ModelGroup;
import cn.asany.shanhai.gateway.bean.ModelGroupItem;
import cn.asany.shanhai.gateway.graphql.types.GraphQLSchema;
import cn.asany.shanhai.gateway.service.ModelGroupService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author limaofeng
 */
@Component
public class GraphQLSchemaGraphQLResolver implements GraphQLResolver<GraphQLSchema> {

    private final ModelService modelService;
    private final ModelGroupService modelGroupService;

    public GraphQLSchemaGraphQLResolver(ModelService modelService, ModelGroupService modelGroupService) {
        this.modelService = modelService;
        this.modelGroupService = modelGroupService;
    }

    public List<ModelField> endpoints(GraphQLSchema schema) {
        return modelService.endpoints();
    }

    public List<ModelField> queries(GraphQLSchema schema) {
        return modelService.queries();
    }

    public List<ModelGroupItem> ungrouped(GraphQLSchema schema) {
        return new ArrayList<>();
    }

    public List<ModelGroup> groups(GraphQLSchema schema) {
        List<ModelGroup> groups = modelGroupService.groups();
        return groups;
    }

}
