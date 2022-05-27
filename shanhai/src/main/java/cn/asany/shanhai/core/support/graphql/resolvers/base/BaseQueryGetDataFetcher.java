package cn.asany.shanhai.core.support.graphql.resolvers.base;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.QueryGetDataFetcher;

public class BaseQueryGetDataFetcher implements QueryGetDataFetcher {

  private ModelRepository repository;

  public BaseQueryGetDataFetcher(Model model, ModelEndpoint endpoint, ModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public Object get(Long id) {
    return this.repository.findById(id);
  }
}
