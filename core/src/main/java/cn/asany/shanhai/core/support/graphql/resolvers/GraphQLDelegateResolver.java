package cn.asany.shanhai.core.support.graphql.resolvers;

import graphql.schema.DataFetchingEnvironment;

public interface GraphQLDelegateResolver {

    String method();

    Object[] args(DataFetchingEnvironment environment);

    String getName();

}
