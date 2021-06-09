package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.graphql.input.ApplicationFilter;
import cn.asany.nuwa.app.service.ApplicationService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应用
 *
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ApplicationService applicationService;

    public Application application(String id, String space) {
        return new Application();
    }

    public List<Application> applications(ApplicationFilter filter) {
        return applicationService.findAll(filter.build());
    }

}
