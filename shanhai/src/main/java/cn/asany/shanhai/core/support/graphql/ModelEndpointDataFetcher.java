package cn.asany.shanhai.core.support.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class ModelEndpointDataFetcher implements DataFetcher<Object> {

  private DelegateHandler delegate;

  public ModelEndpointDataFetcher(DelegateHandler delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object get(DataFetchingEnvironment environment) throws Exception {
    return delegate.invoke(environment);
  }
}
