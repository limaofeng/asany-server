package cn.asany.security.auth.graphql.resolvers;

import cn.asany.security.auth.graphql.types.CurrentUser;
import cn.asany.security.core.bean.enums.IdType;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Set;
import java.util.stream.Collectors;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * 当前用户 解析器
 *
 * @author limaofeng
 */
@Component
public class CurrentUserGraphQLResolver implements GraphQLResolver<CurrentUser> {

  public String uid(CurrentUser user, IdType idType) {
    if (idType == IdType.id) {
      return user.getUid().toString();
    }
    return null;
  }

  public String avatar(CurrentUser user) {
    if (user == null) {
      return null;
    }
    return user.getAvatar();
  }

  public Set<String> authorities(LoginUser user) {
    return user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }
}
