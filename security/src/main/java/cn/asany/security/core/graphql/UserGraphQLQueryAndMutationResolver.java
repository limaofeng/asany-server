package cn.asany.security.core.graphql;

import cn.asany.security.core.bean.User;
import cn.asany.security.core.convert.UserConverter;
import cn.asany.security.core.graphql.input.UserUpdateInput;
import cn.asany.security.core.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

@Component
public class UserGraphQLQueryAndMutationResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final UserService userService;

  private final UserConverter userConverter;

  public UserGraphQLQueryAndMutationResolver(UserService userService, UserConverter userConverter) {
    this.userService = userService;
    this.userConverter = userConverter;
  }

  public User user(Long id) {
    return this.userService.findById(
        ObjectUtil.defaultValue(id, () -> SpringSecurityUtils.getCurrentUser().getUid()));
  }

  public User updateUser(Long id, UserUpdateInput input) {
    User user = this.userConverter.toUser(input);
    return this.userService.update(
        ObjectUtil.defaultValue(id, () -> SpringSecurityUtils.getCurrentUser().getUid()), user);
  }

  public Boolean changePassword(String oldPassword, String newPassword) {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    userService.changePassword(loginUser.getUid(), oldPassword, newPassword);
    return Boolean.TRUE;
  }
}
