package cn.asany.shanhai.core.support.graphql.resolvers;

import cn.asany.shanhai.core.support.graphql.resolvers.base.utils.MethodArgumentResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Map;

import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

public interface QueryFindAllDataFetcher extends DelegateDataFetcher, BaseDataFetcher {

  @Override
  default String getName() {
    return "获取单个对象";
  }

  @Override
  default String method() {
    return "findAll";
  }

  @Override
  default Object[] args(DataFetchingEnvironment environment) {
    Map<String, Object> where = environment.getArgument("where");
    return new Object[] {MethodArgumentResolver.where(where)};
  }

  List<Object> findAll(PropertyFilter filter);
}
