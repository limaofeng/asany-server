package cn.asany.flowable.core.graphql.resolver;

import cn.asany.flowable.core.domain.ProcessModel;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class ProcessModelGraphQLResolver implements GraphQLResolver<ProcessModel> {

    public String editorJson(ProcessModel processModel) {
        return processModel.getModelEditorJson();
    }

}
