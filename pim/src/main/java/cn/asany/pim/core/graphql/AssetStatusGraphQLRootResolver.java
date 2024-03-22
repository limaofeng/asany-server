package cn.asany.pim.core.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class AssetStatusGraphQLRootResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {}
