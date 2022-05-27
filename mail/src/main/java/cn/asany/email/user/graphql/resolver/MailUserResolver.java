package cn.asany.email.user.graphql.resolver;

import cn.asany.email.user.domain.MailUser;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class MailUserResolver implements GraphQLResolver<MailUser> {
  public String id(MailUser user) {
    return user.getName();
  }
}
