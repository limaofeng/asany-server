package cn.asany.shanhai.core.support.graphql.resolvers.base;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.QueryFindAllDataFetcher;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

/**
 * 查询所有数据
 *
 * @author limaofeng
 */
public class BaseQueryFindAllDataFetcher implements QueryFindAllDataFetcher {

  private ModelRepository repository;

  public BaseQueryFindAllDataFetcher(
      Model model, ModelEndpoint endpoint, ModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Object> findAll(PropertyFilter filter) {
    return this.repository.findAll(filter);
  }
}
