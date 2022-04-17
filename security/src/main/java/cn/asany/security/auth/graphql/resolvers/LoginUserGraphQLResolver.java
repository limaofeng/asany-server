package cn.asany.security.auth.graphql.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import java.util.Set;
import java.util.stream.Collectors;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.stereotype.Component;

/**
 * LoginUser Resolver
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-08 16:54
 */
@Component
public class LoginUserGraphQLResolver implements GraphQLResolver<LoginUser> {

  public Set<String> authorities(LoginUser user) {
    return user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  public String token(LoginUser user) {
    AbstractOAuth2Token accessToken = user.getAttribute("token");
    return accessToken != null ? accessToken.getTokenValue() : null;
  }

  public String account(LoginUser user) {
    return user.getUsername();
  }
}
