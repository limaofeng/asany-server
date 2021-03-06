package cn.asany.security.auth.graphql.resolvers;

import cn.asany.base.common.domain.Email;
import cn.asany.base.common.domain.Phone;
import cn.asany.security.auth.graphql.types.CurrentUser;
import cn.asany.security.core.domain.enums.IdType;
import cn.asany.security.core.domain.enums.UserType;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * 当前用户 解析器
 *
 * @author limaofeng
 */
@Component
public class CurrentUserGraphQLResolver implements GraphQLResolver<CurrentUser> {

  public Object uid(CurrentUser user, IdType idType) {
    if (idType == IdType.id) {
      return user.getId();
    }
    return null;
  }

  public UserType type(CurrentUser user) {
    return user.getUserType();
  }

  public String account(CurrentUser user) {
    return user.getUsername();
  }

  public Optional<String> phone(CurrentUser user) {
    if (user.getPhone() == null) {
      return Optional.empty();
    }
    return Optional.of(user.getPhone()).map(Phone::getNumber);
  }

  public Optional<String> email(CurrentUser user) {
    if (user.getEmail() == null) {
      return Optional.empty();
    }
    return Optional.of(user.getEmail()).map(Email::getAddress);
  }

  public Set<String> authorities(CurrentUser user) {
    return user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }
}
