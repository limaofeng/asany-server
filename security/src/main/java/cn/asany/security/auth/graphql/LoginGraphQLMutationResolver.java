package cn.asany.security.auth.graphql;

import cn.asany.security.auth.authentication.DingtalkAuthenticationToken;
import cn.asany.security.auth.authentication.LoginAuthenticationDetails;
import cn.asany.security.auth.authentication.WeChatAuthenticationToken;
import cn.asany.security.auth.graphql.types.LoginOptions;
import cn.asany.security.auth.graphql.types.LoginType;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.security.AuthenticationManager;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.authentication.AbstractAuthenticationToken;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.BadCredentialsException;
import org.jfantasy.framework.security.authentication.UsernamePasswordAuthenticationToken;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.core.OAuth2Authentication;
import org.jfantasy.framework.security.oauth2.core.OAuth2AuthenticationDetails;
import org.jfantasy.framework.security.oauth2.core.TokenType;
import org.jfantasy.framework.security.oauth2.core.token.AuthorizationServerTokenServices;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

/**
 * 登录服务
 *
 * @author limaofeng
 * @version V1.0
 */
@Component
@Slf4j
public class LoginGraphQLMutationResolver implements GraphQLMutationResolver {

  private final AuthenticationManager authenticationManager;
  private final AuthorizationServerTokenServices tokenServices;

  public LoginGraphQLMutationResolver(
      AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices) {
    this.authenticationManager = authenticationManager;
    this.tokenServices = tokenServices;
  }

  /**
   * 登录接口
   *
   * @param loginType 登录方式
   * @param clientId 客户端 ID
   * @param username 用户名
   * @param password 密码
   * @param authCode 授权码
   * @param tmpAuthCode 临时授权码
   * @param options 选项
   * @return 登录用户
   */
  public LoginUser login(
      LoginType loginType,
      String clientId,
      String username,
      String password,
      String authCode,
      String tmpAuthCode,
      LoginOptions options) {
    Authentication token =
        buildAuthenticationToken(loginType, username, password, authCode, tmpAuthCode, options);

    Authentication authentication = authenticationManager.authenticate(token);

    if (!authentication.isAuthenticated()) {
      throw new BadCredentialsException("Bad credentials");
    }
    LoginUser loginUser = (LoginUser) authentication.getPrincipal();
    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails();
    oAuth2AuthenticationDetails.setClientId(clientId);
    oAuth2AuthenticationDetails.setTokenType(TokenType.SESSION);
    OAuth2Authentication oAuth2Authentication =
        new OAuth2Authentication(authentication, oAuth2AuthenticationDetails);
    OAuth2AccessToken accessToken = tokenServices.createAccessToken(oAuth2Authentication);
    loginUser.setAttribute("token", accessToken);
    return loginUser;
  }

  private Authentication buildAuthenticationToken(
      LoginType loginType,
      String username,
      String password,
      String authCode,
      String tmpAuthCode,
      LoginOptions options) {
    options = ObjectUtil.defaultValue(options, LoginOptions.builder().build());
    LoginAuthenticationDetails details = new LoginAuthenticationDetails(loginType, options);
    AbstractAuthenticationToken authentication;

    switch (loginType) {
      case password:
        authentication = new UsernamePasswordAuthenticationToken(username, password);
        break;
      case dingtalk:
        authentication =
            new DingtalkAuthenticationToken(
                DingtalkAuthenticationToken.DingtalkCredentials.builder()
                    .authCode(authCode)
                    .connected(options.getConnected())
                    .build());
        break;
      case WeChatCP:
        authentication =
            new WeChatAuthenticationToken(
                WeChatAuthenticationToken.WeChatCredentials
                    .builder() /*.type(Oauth2.MobileType.WeChatCP)*/
                    .authCode(authCode)
                    .connected(options.getConnected())
                    .build());
        break;
      case WeChatPM:
        authentication =
            new WeChatAuthenticationToken(
                WeChatAuthenticationToken.WeChatCredentials
                    .builder() /*.type(Oauth2.MobileType.WeChatPM)*/
                    .authCode(authCode)
                    .connected(options.getConnected())
                    .build());
        break;
      default:
        throw new IllegalStateException("Unexpected AuthenticationToken By: " + loginType);
    }

    authentication.setDetails(details);

    return authentication;
  }
}

//        options = ObjectUtil.defaultValue(options, LoginOptions.builder().build());
//        switch (loginType) {
//            case dingtalk:
//                return loginByDingtalk(authCode, options);
//            case WeChatCP:
//                return mobileLogin(Oauth2.MobileType.WeChatCP, authCode);
//            case WeChatPM:
//                return mobileLogin(Oauth2.MobileType.WeChatPM, authCode);
//            case single:
//                Employee model = employeeService.findOneBySN(singleCode);
//                if (model == null) {
//                    return null;
//                }
//                return loginService.toLoginUser(model);
//            case tourist:
//                return touristLogin();
//            default:
//                LoginUser user = loginService.login(username, password);
//                if (StringUtil.isNotBlank(options)) {
//                    syncEmployeeDao.save(SyncEmployee.builder().mobile(user.getPhone())
//                        .mobileId(options.getSnser())
//                        .locId(user.getUid())
//                        .status(SyncStatus.over)
//                        .baseId(options.getPlatform()).build());
//                }
//                if ("ezoffice".equals(user.getType())) {
//                    Optional<Employee> optional = employeeService.getByLink(LinkType.ezoffice,
// user.getUid());
//                    if (optional.isPresent()) {
//                        return loginService.toLoginUser(optional.get());
//                    }
//                    return null;
//                }
//                if (options.getConnected()) {
//                    this.employeeService.connected(user.get(DataConstant.FIELD_EMPLOYEE),
// options.getProvider(), options.getSnser());
//                }
//                return user;
//        }
