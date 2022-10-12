package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.core.support.ModelParser;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactory;
import graphql.schema.DataFetcherFactoryEnvironment;

public class ModelEndpointDataFetcherFactory implements DataFetcherFactory<Object> {
  private final ModelParser modelParser;

  public ModelEndpointDataFetcherFactory(ModelParser modelParser) {
    this.modelParser = modelParser;
  }

  @Override
  public DataFetcher<Object> get(DataFetcherFactoryEnvironment environment) {
    return null; // modelParser.getDataFetcher("");
  }
}
