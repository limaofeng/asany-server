package cn.asany.email.user.graphql;

import cn.asany.email.user.bean.MailUser;
import cn.asany.email.user.bean.toys.UpdateMode;
import cn.asany.email.user.service.MailUserService;
import cn.asany.email.utils.JamesUtil;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Set;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class MailUserGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final MailUserService mailUserService;

  public MailUserGraphqlApiResolver(MailUserService mailUserService) {
    this.mailUserService = mailUserService;
  }

  public Set<String> updateMyFavoriteMailboxes(
      String account, Set<String> mailboxes, UpdateMode mode) {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    String user = StringUtil.defaultValue(account, () -> JamesUtil.getUserName(loginUser));

    return this.mailUserService.updateMyFavoriteMailboxes(account, mailboxes, mode);
  }

  public MailUser mailUser(String account) {
    return this.mailUserService.getMailUser(account);
  }
}
