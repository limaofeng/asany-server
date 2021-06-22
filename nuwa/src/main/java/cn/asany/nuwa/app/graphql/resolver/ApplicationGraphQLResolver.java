package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.bean.ClientSecret;
import cn.asany.nuwa.app.service.ApplicationService;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLResolver implements GraphQLResolver<Application> {

    private final ApplicationService applicationService;

    public ApplicationGraphQLResolver(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public ApplicationRoute route(Application application, String space, String path) {
        return null;
    }

    public ApplicationRoute layoutRoute(Application application, String space) {
        return null;
    }

    public ApplicationRoute loginRoute(Application application, String space) {
        return null;
    }

    public ApplicationRoute rootRoute(Application application, String space) {
        return null;
    }

    public List<ApplicationRoute> routes(Application application, String space, DataFetchingEnvironment environment) {
        String parentArgsBySpace = environment.getExecutionStepInfo().getParent().getArgument("space");
        if (space.equals(parentArgsBySpace)) {
            return application.getRoutes();
        }
        return this.applicationService.findRouteAllByApplicationAndSpaceWithComponent(application.getId(), space);
    }

    public Boolean dingtalkIntegration(Application application) {
        return Boolean.FALSE;
    }

//    public Organization organization(Application application) {
//        return null;
//    }

    public List<ClientSecret> clientSecrets(Application application) {
        return application.getClientSecretsAlias();
    }

}
