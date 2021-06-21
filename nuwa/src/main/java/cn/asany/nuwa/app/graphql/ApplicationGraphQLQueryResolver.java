package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.graphql.input.ApplicationFilter;
import cn.asany.nuwa.app.graphql.type.ApplicationIdType;
import cn.asany.nuwa.app.service.ApplicationService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 应用
 *
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLQueryResolver implements GraphQLQueryResolver {

    private final ApplicationService applicationService;

    public ApplicationGraphQLQueryResolver(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public Optional<Application> application(String id, ApplicationIdType idType, String space) {
        if (ApplicationIdType.CLIENT_ID.equals(idType)) {
            return applicationService.findByClientIdWithRoute(id, space);
        }
        return applicationService.findByIdWithRoute(Long.valueOf(id), space);
    }

    public List<Application> applications(ApplicationFilter filter) {
        return applicationService.findAll(filter.build());
    }

}
