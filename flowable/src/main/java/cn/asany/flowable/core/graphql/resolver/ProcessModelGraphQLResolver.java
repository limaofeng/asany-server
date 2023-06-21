package cn.asany.flowable.core.graphql.resolver;

import cn.asany.flowable.core.domain.ProcessModel;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * 工作流/流程模型
 *
 * @author limaofeng
 */
@Component
public class ProcessModelGraphQLResolver implements GraphQLResolver<ProcessModel> {

  public String editorJson(ProcessModel processModel) {
    return processModel.getModelEditorJson();
  }
  
}
