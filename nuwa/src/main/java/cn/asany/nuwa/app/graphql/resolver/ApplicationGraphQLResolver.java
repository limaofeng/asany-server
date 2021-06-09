package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ClientSecret;
import cn.asany.nuwa.app.bean.Route;
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

    public Route route(Application application, String path) {
        return null;
    }

    public Route layoutRoute(Application application, String space) {
        return null;
    }

    public Route loginRoute(Application application, String space) {
        return null;
    }

    public Route rootRoute(Application application, String space) {
        return null;
    }

    public List<Route> routes(Application application, String space) {
        return new ArrayList<>();
    }

    public List<Routespace> routespaces(Application application) {
        return new ArrayList<>();
    }

    public Boolean dingtalkIntegration(Application application) {
        return Boolean.FALSE;
    }

    public List<ClientSecret> clientSecrets(Application application) {
        return application.getClientSecretsAlias();
    }

}
