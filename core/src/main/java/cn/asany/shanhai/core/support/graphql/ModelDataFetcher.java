package cn.asany.shanhai.core.support.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class ModelDataFetcher implements DataFetcher {
    private DelegateHandler delegate;

    public ModelDataFetcher(DelegateHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object get(DataFetchingEnvironment environment) throws Exception {
        return delegate.invoke(environment);
    }
}
