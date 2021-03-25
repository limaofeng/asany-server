package cn.asany.shanhai.core.support.graphql.resolvers;

import graphql.schema.DataFetchingEnvironment;

public interface GraphQLGetQueryResolver extends GraphQLDelegateResolver {

    default String method() {
        return "get";
    }

    default Object[] args(DataFetchingEnvironment environment) {
        String id = environment.getArgument("id");
        return new Object[]{Long.valueOf(id)};
    }

    Object get(Long id);

}
