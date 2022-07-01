package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.convert.AccessTokenConverter;
import cn.asany.security.oauth.domain.AccessToken;
import cn.asany.security.oauth.service.AccessTokenService;
import cn.asany.security.oauth.service.TokenServiceUtils;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SecurityContextHolder;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.DefaultTokenServices;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.core.OAuth2Authentication;
import org.jfantasy.framework.security.oauth2.core.OAuth2AuthenticationDetails;
import org.jfantasy.framework.security.oauth2.core.TokenType;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author limaofeng
 */
@Component
public class AccessTokenGraphQLMutationResolver implements GraphQLMutationResolver {

  private final AccessTokenService accessTokenService;
  private final TokenServiceUtils tokenServiceUtils;

  public AccessTokenGraphQLMutationResolver(
      AccessTokenService accessTokenService, TokenServiceUtils tokenServiceUtils) {
    this.accessTokenService = accessTokenService;
    this.tokenServiceUtils = tokenServiceUtils;
  }

  public PersonalAccessToken createPersonalAccessToken(String clientId, String name) {
    return this.tokenServiceUtils.createPersonalAccessToken(clientId, name);
  }

  public boolean revokeSession(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.tokenServiceUtils.revokeSession(user.getUid(), id);
  }

  public boolean revokePersonalAccessToken(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.tokenServiceUtils.revokePersonalAccessToken(user.getUid(), id);
  }

  public boolean revokeToken(String token) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return tokenServiceUtils.revokeToken(user.getUid(), token);
  }
}
