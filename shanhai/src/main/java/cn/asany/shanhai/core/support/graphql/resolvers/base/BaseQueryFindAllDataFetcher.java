package cn.asany.shanhai.core.support.graphql.resolvers.base;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.QueryFindAllDataFetcher;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

public class BaseQueryFindAllDataFetcher implements QueryFindAllDataFetcher {

  private ModelRepository repository;

  public BaseQueryFindAllDataFetcher(
      Model model, ModelEndpoint endpoint, ModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Object> findAll(PropertyFilterBuilder filter) {
    return this.repository.findAll(filter.build());
  }
}
