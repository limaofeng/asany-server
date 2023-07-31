package cn.asany.email.user.graphql;

import cn.asany.email.user.domain.MailUser;
import cn.asany.email.user.graphql.type.MailUserIdType;
import cn.asany.email.user.service.MailUserService;
import cn.asany.email.utils.JamesUtil;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Optional;
import java.util.Set;
import org.apache.james.mailbox.exception.MailboxException;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.graphql.UpdateMode;
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
    return this.mailUserService.updateMyFavoriteMailboxes(user, mailboxes, mode);
  }

  public Optional<MailUser> mailUser(String user, MailUserIdType type) throws MailboxException {
    if (StringUtil.isBlank(user) || type == MailUserIdType.LOGIN_USER_ID) {
      if (user != null && RegexpUtil.isMatch(user, RegexpConstant.VALIDATOR_INTEGE)) {
        user = JamesUtil.getUserNameByUserId(Long.parseLong(user));
      } else {
        user = JamesUtil.getUserName(SpringSecurityUtils.getCurrentUser());
      }
    }
    return this.mailUserService.findById(user);
  }
}
