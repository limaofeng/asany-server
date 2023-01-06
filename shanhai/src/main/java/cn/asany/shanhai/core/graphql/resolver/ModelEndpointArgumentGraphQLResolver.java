package cn.asany.shanhai.core.graphql.resolver;

import cn.asany.shanhai.core.domain.ModelEndpointArgument;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class ModelEndpointArgumentGraphQLResolver
    implements GraphQLResolver<ModelEndpointArgument> {}
