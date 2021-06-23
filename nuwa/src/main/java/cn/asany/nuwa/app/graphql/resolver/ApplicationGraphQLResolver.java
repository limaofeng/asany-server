package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.bean.ClientSecret;
import cn.asany.nuwa.app.service.ApplicationService;
import graphql.execution.ExecutionStepInfo;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLResolver implements GraphQLResolver<Application> {

    private final ApplicationService applicationService;

    public ApplicationGraphQLResolver(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public Optional<ApplicationRoute> route(Application application, String space, String path, DataFetchingEnvironment environment) {
        List<ApplicationRoute> routes = this.routes(application, space, environment);
        return routes.stream().filter(item -> path.equals(item.getPath())).findAny();
    }

    public Optional<ApplicationRoute> rootRoute(Application application, String space, DataFetchingEnvironment environment) {
        List<ApplicationRoute> routes = this.routes(application, space, environment);
        return routes.stream().filter(item -> "/".equals(item.getPath()) && item.getLevel() == 0).findAny();
    }

    public Optional<ApplicationRoute> layoutRoute(Application application, String space, DataFetchingEnvironment environment) {
        List<ApplicationRoute> routes = this.routes(application, space, environment);
        return routes.stream().filter(item -> "/".equals(item.getPath()) && item.getLevel() == 1).findAny();
    }

    public Optional<ApplicationRoute> loginRoute(Application application, String space, DataFetchingEnvironment environment) {
        List<ApplicationRoute> routes = this.routes(application, space, environment);
        return routes.stream().filter(item -> "/login".equals(item.getPath()) && item.getLevel() == 1).findAny();
    }

    public List<ApplicationRoute> routes(Application application, String space, DataFetchingEnvironment environment) {
        ExecutionStepInfo parent = environment.getExecutionStepInfo().getParent();
        if ("application".equals(parent.getFieldDefinition().getName())) {
            return application.getRoutes().stream().filter(item -> space.equals(item.getSpace().getId())).collect(Collectors.toList());
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
