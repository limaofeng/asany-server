package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.service.AccessTokenService;
import cn.asany.security.oauth.service.TokenServiceUtils;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.springframework.stereotype.Component;

/** @author limaofeng */
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
