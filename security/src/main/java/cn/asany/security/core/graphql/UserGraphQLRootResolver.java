package cn.asany.security.core.graphql;

import cn.asany.base.common.domain.Phone;
import cn.asany.base.common.domain.enums.PhoneNumberStatus;
import cn.asany.base.sms.CaptchaService;
import cn.asany.security.auth.utils.AuthUtils;
import cn.asany.security.core.convert.UserConverter;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.domain.enums.UserType;
import cn.asany.security.core.event.RegisterSuccessEvent;
import cn.asany.security.core.graphql.input.UserUpdateInput;
import cn.asany.security.core.service.UserService;
import cn.asany.storage.api.FileObject;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.security.authentication.SimpleAuthenticationToken;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.core.OAuth2Authentication;
import org.jfantasy.framework.security.oauth2.core.token.AuthorizationServerTokenServices;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UserGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final UserService userService;

  private final UserConverter userConverter;

  private final CaptchaService captchaService;

  private final ApplicationEventPublisher publisher;

  private final AuthorizationServerTokenServices tokenServices;

  public UserGraphQLRootResolver(
      UserService userService,
      UserConverter userConverter,
      CaptchaService captchaService,
      ApplicationEventPublisher publisher,
      AuthorizationServerTokenServices tokenServices) {
    this.userService = userService;
    this.userConverter = userConverter;
    this.captchaService = captchaService;
    this.publisher = publisher;
    this.tokenServices = tokenServices;
  }

  /**
   * 注册
   *
   * @param nickName 昵称
   * @param avatar 头像
   * @param phoneNumber 电话号码
   * @param password 密码
   * @param smsCode 短信验证码
   * @return User
   */
  public LoginUser register(
      String nickName,
      FileObject avatar,
      String phoneNumber,
      String password,
      String smsCode,
      DataFetchingEnvironment environment) {

    AuthorizationGraphQLServletContext context = environment.getContext();

    //    if (!this.captchaService.validateResponseForID(
    //        CaptchaSource.CAPTCHA_CONFIG_ID,
    //        CaptchaSource.getSessionId(phoneNumber, CaptchaSource.LOGIN),
    //        smsCode)) {
    //      return null;
    //    }

    User user =
        User.builder()
            .nickName(nickName)
            .avatar(avatar)
            .userType(UserType.USER)
            .username(phoneNumber)
            .password(password)
            .phone(Phone.builder().number(phoneNumber).status(PhoneNumberStatus.VERIFIED).build())
            .build();

    LoginUser loginUser = this.userService.register(user);

    SimpleAuthenticationToken authentication = new SimpleAuthenticationToken(loginUser);
    authentication.setDetails(context.getAuthentication().getDetails());

    String clientId = context.getRequest().getHeader("X-Client-ID");

    OAuth2Authentication oauth2 =
        AuthUtils.buildOAuth2(clientId, context.getRequest(), authentication);

    OAuth2AccessToken accessToken = tokenServices.createAccessToken(oauth2);

    loginUser.setAttribute("token", accessToken);
    publisher.publishEvent(new RegisterSuccessEvent(loginUser));

    return loginUser;
  }

  public User user(Long id) {
    return this.userService.get(
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

  public User getUser(Long id) {
    return null;
  }
}
