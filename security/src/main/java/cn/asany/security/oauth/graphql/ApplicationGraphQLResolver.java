package cn.asany.security.oauth.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
class ApplicationMutationGraphQLResolver implements GraphQLMutationResolver {

    private AppService appService;

    public Application createApplication(ApplicationCreateInput input) {
        return null;
    }

}
