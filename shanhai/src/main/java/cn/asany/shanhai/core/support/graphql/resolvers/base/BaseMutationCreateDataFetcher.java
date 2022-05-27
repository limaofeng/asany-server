package cn.asany.shanhai.core.support.graphql.resolvers.base;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.MutationCreateDataFetcher;

public class BaseMutationCreateDataFetcher implements MutationCreateDataFetcher {

  private ModelRepository repository;

  public BaseMutationCreateDataFetcher(
      Model model, ModelEndpoint endpoint, ModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public Object create(Object input) {
    return this.repository.save(input);
  }
}
