package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.bean.AccessToken;
import cn.asany.security.oauth.graphql.inputs.PersonalAccessTokenCreateInput;
import cn.asany.security.oauth.service.AccessTokenService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 */
@Component
public class AccessTokenGraphQLMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private AccessTokenService accessTokenService;

    public AccessToken createPersonalAccessToken(PersonalAccessTokenCreateInput input){
        return this.accessTokenService.createPersonalAccessToken(input.getClientId(), input.getName());
    }

}
