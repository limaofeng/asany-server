package cn.asany.security.core.graphql;

import cn.asany.security.core.service.ResourceTypeService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 资源接口
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class ResourceGraphQLQueryAndMutationResolver implements GraphQLMutationResolver {

  private ResourceTypeService resourceTypeService;

  public ResourceGraphQLQueryAndMutationResolver(ResourceTypeService resourceTypeService) {
    this.resourceTypeService = resourceTypeService;
  }
}
