package cn.asany.shanhai.core.support.graphql.resolvers.base;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.MutationDeleteDataFetcher;

/**
 * 删除逻辑
 *
 * @author limaofeng
 */
public class BaseMutationDeleteDataFetcher implements MutationDeleteDataFetcher {

  private ModelRepository repository;

  public BaseMutationDeleteDataFetcher(
      Model model, ModelEndpoint endpoint, ModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public Object delete(Long id) {
    Object entity = this.repository.findById(id);
    if (entity == null) {
      return null;
    }
    this.repository.delete(id);
    return entity;
  }
}
