package cn.asany.shanhai.core.support.graphql.resolvers;

import graphql.schema.DataFetchingEnvironment;

public interface MutationDeleteDataFetcher extends DelegateDataFetcher, BaseDataFetcher {

  @Override
  default String getName() {
    return "删除对象";
  }

  @Override
  default String method() {
    return "delete";
  }

  @Override
  default Object[] args(DataFetchingEnvironment environment) {
    String id = environment.getArgument("id");
    return new Object[] {Long.valueOf(id)};
  }

  Object delete(Long input);
}
