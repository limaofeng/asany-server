package cn.asany.security.auth.graphql;

import cn.asany.security.auth.error.UnauthorizedException;
import cn.asany.security.core.bean.User;
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
 * @date 2019-04-01 15:17
 */
@Component
public class LoginGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private UserService userService;

  /**
   * 获取当前用户
   *
   * @param organization 设置当前组织
   * @param environment 上下文对象
   * @return
   * @throws UserNotFoundException
   */
  public LoginUser viewer(String organization, DataFetchingEnvironment environment)
      throws UserNotFoundException {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    if (loginUser == null) {
      throw new UnauthorizedException("需要登录");
    }
    Optional<User> optionalUser = userService.get(Long.valueOf(loginUser.getUid()));
    return userService.buildLoginUser(
        optionalUser.orElseThrow(() -> new UserNotFoundException(loginUser.getUid() + "对应的用户不存在")));
  }
}
