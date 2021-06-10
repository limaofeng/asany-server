package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.graphql.input.ApplicationCreateInput;
import cn.asany.nuwa.app.graphql.input.RouteCreateInput;
import cn.asany.nuwa.app.graphql.input.RouteUpdateInput;
import cn.asany.nuwa.app.service.ApplicationService;
import cn.asany.nuwa.app.service.dto.OAuthApplication;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 应用 Mutation
 *
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private ApplicationService applicationService;

    public Application createApplication(ApplicationCreateInput input) {
        return applicationService.createApplication(new OAuthApplication());
    }

    public Application updateApplication(Long id, ApplicationCreateInput input, Boolean merge) {
        return new Application();
    }

    public Boolean removeApplication(Long id) {
        return Boolean.TRUE;
    }

    public ApplicationRoute createRoute(RouteCreateInput input) {
        return null;
    }

    public ApplicationRoute updateRoute(Long id, RouteUpdateInput input, Boolean merge) {
        return null;
    }

    public ApplicationRoute removeRoute(Long id) {
        return null;
    }

    public ApplicationRoute moveRoute(Long id, Long parentRoute, int location) {
        return null;
    }


}
