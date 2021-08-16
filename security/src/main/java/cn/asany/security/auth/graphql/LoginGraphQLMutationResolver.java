package cn.asany.security.auth.graphql;

import cn.asany.security.auth.authentication.*;
import cn.asany.security.auth.graphql.types.LoginOptions;
import cn.asany.security.auth.graphql.types.LoginType;
import graphql.GraphQLError;
import graphql.kickstart.spring.error.ErrorContext;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.security.AuthenticationException;
import org.jfantasy.framework.security.AuthenticationManager;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.authentication.AbstractAuthenticationToken;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.BadCredentialsException;
import org.jfantasy.framework.security.authentication.UsernamePasswordAuthenticationToken;
import org.jfantasy.framework.security.oauth2.core.*;
import org.jfantasy.framework.security.oauth2.core.token.AuthorizationServerTokenServices;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.jfantasy.graphql.context.GraphQLContextHolder;
import org.jfantasy.graphql.util.GraphQLErrorUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 登录服务
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-01 15:17
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

  @ExceptionHandler(value = AuthenticationException.class)
  public GraphQLError loginExceptionHandler(AuthenticationException e, ErrorContext context) {
    AuthorizationGraphQLServletContext servletContext =
        (AuthorizationGraphQLServletContext) GraphQLContextHolder.getContext();
    servletContext.getResponse().setStatus(401);
    return GraphQLErrorUtils.buildGraphQLError(context, e);
  }

  public LoginUser login(
      LoginType loginType,
      String username,
      String password,
      String authCode,
      String tmpAuthCode,
      String singleCode,
      LoginOptions options,
      DataFetchingEnvironment environment) {
    Authentication token =
        buildAuthenticationToken(
            loginType, username, password, authCode, tmpAuthCode, singleCode, options);

    Authentication authentication = authenticationManager.authenticate(token);

    if (!authentication.isAuthenticated()) {
      throw new BadCredentialsException("Bad credentials");
    }
    LoginUser loginUser = (LoginUser) authentication.getPrincipal();
    AuthorizationGraphQLServletContext context = environment.getContext();
    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails();
    oAuth2AuthenticationDetails.setClientId("8b2ef1b9625d233ad852");
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
      String singleCode,
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
      case tourist:
        authentication = new AnonymousAuthenticationToken();
        break;
      case single:
        authentication = new TemporaryAuthenticationToken(singleCode);
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
