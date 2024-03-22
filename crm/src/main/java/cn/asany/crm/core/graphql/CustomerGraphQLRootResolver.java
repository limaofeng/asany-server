package cn.asany.crm.core.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class CustomerGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {}
