package cn.asany.shanhai.core.support.graphql.resolvers;

import graphql.schema.DataFetchingEnvironment;
import java.util.HashMap;
import java.util.Map;

public interface MutationCreateDataFetcher extends DelegateDataFetcher, BaseDataFetcher {

  @Override
  default String getName() {
    return "保存对象";
  }

  @Override
  default String method() {
    return "create";
  }

  @Override
  default Object[] args(DataFetchingEnvironment environment) {
    Map<String, Object> input = new HashMap<>();
    Map arg = environment.getArgument("input");
    input.putAll(arg);
    return new Object[] {input};
  }

  Object create(Object input);
}
