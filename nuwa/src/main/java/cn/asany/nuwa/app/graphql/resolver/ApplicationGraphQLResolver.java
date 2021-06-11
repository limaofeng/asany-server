package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.bean.ClientSecret;
import cn.asany.nuwa.app.bean.Routespace;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLResolver implements GraphQLResolver<Application> {

    public ApplicationRoute route(Application application, String path) {
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

    public List<ApplicationRoute> routes(Application application, String space) {
        return new ArrayList<>();
    }

    public List<Routespace> routespaces(Application application) {
        return new ArrayList<>();
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
