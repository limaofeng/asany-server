package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class ModelDataFetcher implements DataFetcher {

    private ModelRepository repository;
    private ModelEndpoint endpoint;

    public ModelDataFetcher(ModelEndpoint endpoint, ModelRepository repository) {
        this.repository = repository;
        this.endpoint = endpoint;
    }

    @Override
    public Object get(DataFetchingEnvironment environment) throws Exception {
        return null;
    }
}
