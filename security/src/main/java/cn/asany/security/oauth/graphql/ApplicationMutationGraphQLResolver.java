package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.bean.OAuthApplication;
import cn.asany.security.oauth.graphql.input.ApplicationCreateInput;
import cn.asany.security.oauth.service.OAuthApplicationService;
import graphql.kickstart.tools.GraphQLMutationResolver;

public class ApplicationMutationGraphQLResolver implements GraphQLMutationResolver {

    private OAuthApplicationService OAuthApplicationService;

    public OAuthApplication createApplication(ApplicationCreateInput input) {
        return null;
    }

}
