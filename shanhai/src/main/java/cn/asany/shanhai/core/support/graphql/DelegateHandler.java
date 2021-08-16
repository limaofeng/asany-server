package cn.asany.shanhai.core.support.graphql;

import graphql.schema.DataFetchingEnvironment;

public interface DelegateHandler {

  Object invoke(DataFetchingEnvironment environment);
}
