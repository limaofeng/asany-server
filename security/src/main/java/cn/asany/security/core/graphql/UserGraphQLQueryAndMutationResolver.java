package cn.asany.security.core.graphql;

import cn.asany.security.core.bean.User;
import cn.asany.security.core.convert.UserConverter;
import cn.asany.security.core.graphql.inputs.UserUpdateInput;
import cn.asany.security.core.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserGraphQLQueryAndMutationResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final UserService userService;

  @Autowired private UserConverter userConverter;

  public UserGraphQLQueryAndMutationResolver(UserService userService) {
    this.userService = userService;
  }

  public User updateUser(Long id, UserUpdateInput input, Boolean merge) {
    User user = this.userConverter.toUser(input);
    return this.userService.update(
        ObjectUtil.defaultValue(id, () -> SpringSecurityUtils.getCurrentUser().getUid()),
        user,
        merge);
  }

  public Boolean changePassword(String oldPassword, String newPassword) {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    userService.changePassword(loginUser.getUid(), oldPassword, newPassword);
    return Boolean.TRUE;
  }
}
