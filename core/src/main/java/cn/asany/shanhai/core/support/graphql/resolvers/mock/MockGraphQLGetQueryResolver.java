package cn.asany.shanhai.core.support.graphql.resolvers.mock;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.GraphQLGetQueryResolver;

public class MockGraphQLGetQueryResolver implements GraphQLGetQueryResolver {

    private ModelRepository repository;

    public MockGraphQLGetQueryResolver(Model model, ModelEndpoint endpoint, ModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getName() {
        return "获取单个对象";
    }

    public Object get(Long id) {
        return this.repository.findById(id);
    }

}
