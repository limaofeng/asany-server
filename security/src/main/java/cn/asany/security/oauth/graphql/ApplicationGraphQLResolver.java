package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.bean.Application;
import cn.asany.security.oauth.graphql.inputs.ApplicationCreateInput;
import cn.asany.security.oauth.service.AppService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
class ApplicationMutationGraphQLResolver implements GraphQLMutationResolver {

    private AppService appService;

    public Application createApplication(ApplicationCreateInput input) {
        return null;
    }

}
