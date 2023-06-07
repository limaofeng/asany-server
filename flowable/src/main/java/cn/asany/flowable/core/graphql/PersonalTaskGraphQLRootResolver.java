package cn.asany.flowable.core.graphql;

import cn.asany.flowable.core.service.PersonalTaskService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class PersonalTaskGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final PersonalTaskService personalTaskService;

  public PersonalTaskGraphQLRootResolver(PersonalTaskService personalTaskService) {
    this.personalTaskService = personalTaskService;
  }

  public Boolean createTask() {
//    personalTaskService.createTask(1L, "", "");
    return Boolean.TRUE;
  }
}
