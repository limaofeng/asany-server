package cn.asany.security.core.service;

import cn.asany.security.core.domain.User;
import cn.asany.security.core.util.UserUtil;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.dataloader.DataLoader;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.core.userdetails.UserDetailsService;
import org.jfantasy.framework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * 用户服务
 *
 * @author limaofeng
 */
public class DefaultUserDetailsService implements UserDetailsService<LoginUser> {

  private final UserService userService;
  protected final MessageSourceAccessor messages;
  private final DataLoader<Long, User> userDataLoader;

  public DefaultUserDetailsService(
      UserService userService,
      DataLoader<Long, User> userDataLoader,
      MessageSourceAccessor messages) {
    this.userService = userService;
    this.messages = messages;
    this.userDataLoader = userDataLoader;
  }

  @Override
  public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optional = this.userService.findOneByUsername(username);

    if (!optional.isPresent()) {
      optional = userService.findOneByPhone(username);
    }

    // 用户不存在
    if (!optional.isPresent()) {
      throw new UsernameNotFoundException(
          this.messages.getMessage(
              "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }
    return UserUtil.buildLoginUser(optional.get());
  }

  @Override
  public CompletableFuture<LoginUser> loadUserById(Long id) {
    return userDataLoader.load(id).thenApply(UserUtil::buildLoginUser);
  }
}
