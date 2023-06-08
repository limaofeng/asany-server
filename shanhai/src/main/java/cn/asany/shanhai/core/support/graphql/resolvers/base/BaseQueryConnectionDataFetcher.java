package cn.asany.shanhai.core.support.graphql.resolvers.base;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.QueryFindAllDataFetcher;
import java.util.List;

import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

public class BaseQueryConnectionDataFetcher implements QueryFindAllDataFetcher {

  private ModelRepository repository;

  public BaseQueryConnectionDataFetcher(
      Model model, ModelEndpoint endpoint, ModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Object> findAll(PropertyFilter filter) {
    return this.repository.findAll(filter);
  }
}
