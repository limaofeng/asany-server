package cn.asany.flowable.core.graphql;

import cn.asany.flowable.core.service.ProcessDefinitionService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * 流程定义
 *
 * @author limaofeng
 */
@Component
public class ProcessDefinitionGraphQLRootResolver implements GraphQLQueryResolver {

  private final ProcessDefinitionService processDefinitionService;

  public ProcessDefinitionGraphQLRootResolver(ProcessDefinitionService processDefinitionService) {
    this.processDefinitionService = processDefinitionService;
  }

  /**
   * 分页查询流程定义
   *
   * @return Integer
   */
  public Integer processDefinitions() {
    processDefinitionService.list("", "");
    return 0;
  }
}
