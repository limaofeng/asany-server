package cn.asany.shanhai.core.support.graphql.resolvers.base;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.MutationUpdateDataFetcher;

/**
 * 删除逻辑
 *
 * @author limaofeng
 */
public class BaseMutationUpdateDataFetcher implements MutationUpdateDataFetcher {

  private ModelRepository repository;

  public BaseMutationUpdateDataFetcher(
      Model model, ModelEndpoint endpoint, ModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public Object update(Long id, Object input) {
    return this.repository.update(id, input);
  }
}
