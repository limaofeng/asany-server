package cn.asany.security.auth.graphql;

import cn.asany.security.auth.error.UnauthorizedException;
import cn.asany.security.auth.graphql.types.CurrentUser;
import cn.asany.security.core.convert.UserConverter;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.exception.UserNotFoundException;
import cn.asany.security.core.service.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Optional;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录 QueryResolver
 *
 * @author limaofeng
 * @version V1.0
 */
@Component
public class LoginGraphQLQueryResolver implements GraphQLQueryResolver {

  private final UserService userService;

  @Autowired private UserConverter userConverter;

  public LoginGraphQLQueryResolver(UserService userService) {
    this.userService = userService;
  }

  /**
   * 获取当前用户
   *
   * @param environment 上下文对象
   * @return LoginUser 登录用户
   * @throws UserNotFoundException 用户不存在
   */
  public CurrentUser viewer(DataFetchingEnvironment environment) throws UserNotFoundException {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    if (loginUser == null) {
      throw new UnauthorizedException("需要登录");
    }
    Optional<User> optionalUser = userService.get(loginUser.getUid());

    return userConverter.toCurrentUser(
        optionalUser.orElseThrow(() -> new UserNotFoundException(loginUser.getUid() + "对应的用户不存在")));
  }
}
