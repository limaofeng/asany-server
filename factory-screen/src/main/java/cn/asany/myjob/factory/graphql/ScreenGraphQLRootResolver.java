package cn.asany.myjob.factory.graphql;

import cn.asany.myjob.factory.convert.ScreenConverter;
import cn.asany.myjob.factory.domain.Screen;
import cn.asany.myjob.factory.graphql.input.ScreenCreateInput;
import cn.asany.myjob.factory.graphql.input.ScreenUpdateInput;
import cn.asany.myjob.factory.graphql.subscription.ScreenChangePublisher;
import cn.asany.myjob.factory.service.ScreenService;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.exception.UserNotFoundException;
import cn.asany.security.core.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.security.crypto.password.PasswordEncoder;
import net.asany.jfantasy.framework.util.common.DateUtil;
import net.asany.jfantasy.framework.util.web.WebUtil;
import net.asany.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
public class ScreenGraphQLRootResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver, GraphQLSubscriptionResolver {

  private final ScreenService screenService;
  private final ScreenConverter screenConverter;

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final ScreenChangePublisher screenChangePublisher;

  public ScreenGraphQLRootResolver(
      ScreenService screenService,
      UserService userService,
      PasswordEncoder passwordEncoder,
      ScreenConverter screenConverter,
      ScreenChangePublisher screenChangePublisher) {
    this.screenService = screenService;
    this.screenConverter = screenConverter;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.screenChangePublisher = screenChangePublisher;
  }

  public Date ping() {
    return DateUtil.now();
  }

  public String getClientIp(DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();
    return WebUtil.getRealIpAddress(context.getRequest());
  }

  public Optional<Screen> getScreenForIp(String ip) {
    return this.screenService.findScreenForIp(ip);
  }

  public List<Screen> screens() {
    return screenService.findAll();
  }

  public Optional<Screen> screen(Long id) {
    return this.screenService.findById(id);
  }

  public Boolean validateAdminKey(String key) {
    User admin =
        this.userService
            .findOneByUsername("admin")
            .orElseThrow(() -> new UserNotFoundException("管理账户异常"));
    return passwordEncoder.matches(key, admin.getPassword());
  }

  public Screen createScreen(
      @Validated ScreenCreateInput input, DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();
    Screen screen = screenConverter.toScreen(input);
    if (input.getBoundIp() == null) {
      screen.setBoundIp(WebUtil.getRealIpAddress(context.getRequest()));
    }
    return this.screenService.save(screen);
  }

  public Screen updateScreen(Long id, ScreenUpdateInput input, Boolean merge) {
    return this.screenService.update(id, screenConverter.toScreen(input), merge);
  }

  public Screen deleteScreen(Long id) {
    return this.screenService.delete(id);
  }

  Publisher<Screen> onUpdateScreen(Long id, DataFetchingEnvironment env) {
    return screenChangePublisher.getPublisher((screen) -> id == null || screen.getId().equals(id));
  }
}
