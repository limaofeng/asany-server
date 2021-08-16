package cn.asany.shanhai.core.support.graphql.resolvers;

import graphql.schema.DataFetchingEnvironment;
import java.util.HashMap;
import java.util.Map;

public interface MutationUpdateDataFetcher extends DelegateDataFetcher, BaseDataFetcher {
  @Override
  default String getName() {
    return "更新对象";
  }

  @Override
  default String method() {
    return "update";
  }

  @Override
  default Object[] args(DataFetchingEnvironment environment) {
    Map<String, Object> input = new HashMap<>();
    String id = environment.getArgument("id");
    Map arg = environment.getArgument("input");
    input.putAll(arg);
    return new Object[] {Long.valueOf(id), input};
  }

  Object update(Long id, Object input);
}
