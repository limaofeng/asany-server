package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.bean.Consumer;
import cn.asany.shanhai.core.bean.Service;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceGraphQLQueryResolver implements GraphQLQueryResolver {

    public List<Service> services() {
        return new ArrayList<>();
    }

    public List<Consumer> consumers() {
        return new ArrayList<>();
    }

}
