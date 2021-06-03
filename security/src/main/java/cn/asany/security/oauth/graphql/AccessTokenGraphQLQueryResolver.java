package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.service.AccessTokenService;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import cn.asany.security.oauth.vo.SessionAccessToken;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SecurityContextHolder;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.jfantasy.framework.security.oauth2.server.authentication.BearerTokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author limaofeng
 */
@Component
public class AccessTokenGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AccessTokenService accessTokenService;

    public List<SessionAccessToken> sessions() {
        BearerTokenAuthentication authentication = (BearerTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = SpringSecurityUtils.getCurrentUser();
        JwtTokenPayload payload = JwtUtils.payload(authentication.getToken().getTokenValue());
        return this.accessTokenService.getSessions(payload.getClientId(), Long.valueOf(user.getUid()));
    }

    public List<PersonalAccessToken> personalAccessTokens() {
        BearerTokenAuthentication authentication = (BearerTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = SpringSecurityUtils.getCurrentUser();
        JwtTokenPayload payload = JwtUtils.payload(authentication.getToken().getTokenValue());
        return this.accessTokenService.getPersonalAccessTokens(payload.getClientId(), Long.valueOf(user.getUid()));
    }

}
