package cn.asany.shanhai.gateway.graphql.resolvers;

import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.gateway.bean.ModelGroup;
import cn.asany.shanhai.gateway.graphql.types.GraphQLSchema;
import cn.asany.shanhai.gateway.service.ModelGroupService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GraphQLSchemaGraphQLResolver implements GraphQLResolver<GraphQLSchema> {

    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelGroupService modelGroupService;

    public List<ModelField> queries(GraphQLSchema schema) {
        return modelService.queries();
    }

    public List<ModelGroup> ungrouped(GraphQLSchema schema) {
        return new ArrayList<>();
    }

    public List<ModelGroup> groups(GraphQLSchema schema) {
        return modelGroupService.groups();
    }

}
