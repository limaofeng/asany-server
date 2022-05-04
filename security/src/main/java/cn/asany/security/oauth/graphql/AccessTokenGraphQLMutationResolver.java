package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.service.AccessTokenService;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 */
@Component
public class AccessTokenGraphQLMutationResolver implements GraphQLMutationResolver {

  private final AccessTokenService accessTokenService;

  public AccessTokenGraphQLMutationResolver(AccessTokenService accessTokenService) {
    this.accessTokenService = accessTokenService;
  }

  public PersonalAccessToken createPersonalAccessToken(String clientId, String name) {
    return this.accessTokenService.createPersonalAccessToken(clientId, name);
  }

  public boolean revokeSession(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.accessTokenService.revokeSession(user.getUid(), id);
  }

  public boolean revokePersonalAccessToken(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.accessTokenService.revokePersonalAccessToken(user.getUid(), id);
  }

  public boolean revokeToken(String token) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.accessTokenService.revokeToken(user.getUid(), token);
  }
}
