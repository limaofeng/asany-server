package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.service.ModelService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelGraphQLMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private ModelService modelService;

    public Model createModel(Model input) {
        return modelService.save(input);
    }

}
