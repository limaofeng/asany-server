package cn.asany.shanhai.core.support.graphql.resolvers;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;

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
        return new Object[]{};
    }

    List<Object> findAll();

}
