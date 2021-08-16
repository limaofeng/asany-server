package cn.asany.shanhai.core.support.graphql.resolvers;

import graphql.schema.DataFetchingEnvironment;

public interface QueryGetDataFetcher extends DelegateDataFetcher, BaseDataFetcher {

  @Override
  default String getName() {
    return "获取单个对象";
  }

  @Override
  default String method() {
    return "get";
  }

  @Override
  default Object[] args(DataFetchingEnvironment environment) {
    String id = environment.getArgument("id");
    return new Object[] {Long.valueOf(id)};
  }

  Object get(Long id);
}
