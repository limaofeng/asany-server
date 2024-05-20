/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.security.auth.graphql;

import cn.asany.security.auth.authentication.DingtalkAuthenticationToken;
import cn.asany.security.auth.authentication.LoginAuthenticationDetails;
import cn.asany.security.auth.authentication.WeChatAuthenticationToken;
import cn.asany.security.auth.error.UnauthorizedException;
import cn.asany.security.auth.event.LoginSuccessEvent;
import cn.asany.security.auth.event.LogoutSuccessEvent;
import cn.asany.security.auth.graphql.types.LoginOptions;
import cn.asany.security.auth.graphql.types.LoginType;
import cn.asany.security.auth.utils.AuthUtils;
import cn.asany.security.core.domain.enums.SocialProvider;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.security.AuthenticationManager;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SecurityContextHolder;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import net.asany.jfantasy.framework.security.auth.core.TokenServiceFactory;
import net.asany.jfantasy.framework.security.auth.core.token.AuthorizationServerTokenServices;
import net.asany.jfantasy.framework.security.auth.core.token.ConsumerTokenServices;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AccessToken;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2Authentication;
import net.asany.jfantasy.framework.security.auth.oauth2.server.authentication.BearerTokenAuthentication;
import net.asany.jfantasy.framework.security.authentication.AbstractAuthenticationToken;
import net.asany.jfantasy.framework.security.authentication.Authentication;
import net.asany.jfantasy.framework.security.authentication.BadCredentialsException;
import net.asany.jfantasy.framework.security.authentication.UsernamePasswordAuthenticationToken;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.security.context.AuthGraphQLServletContext;
import org.springframework.context.ApplicationEventPublisher;
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
  private final TokenServiceFactory tokenServiceFactory;
  private final ApplicationEventPublisher publisher;

  public LoginGraphQLMutationResolver(
      AuthenticationManager authenticationManager,
      TokenServiceFactory tokenServiceFactory,
      ApplicationEventPublisher publisher) {
    this.authenticationManager = authenticationManager;
    this.tokenServiceFactory = tokenServiceFactory;
    this.publisher = publisher;
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
      LoginOptions options,
      DataFetchingEnvironment environment) {

    //noinspection deprecation
    AuthGraphQLServletContext context = environment.getContext();

    Authentication token =
        buildAuthenticationToken(
            loginType, username, password, authCode, tmpAuthCode, options, context);

    Authentication authentication = authenticationManager.authenticate(token);

    if (!authentication.isAuthenticated()) {
      throw new BadCredentialsException("Bad credentials");
    }
    LoginUser loginUser = authentication.getPrincipal();

    OAuth2Authentication auth2 =
        AuthUtils.buildOAuth2(clientId, context.getRequest(), authentication);

    AuthorizationServerTokenServices<OAuth2AccessToken> tokenServices =
        tokenServiceFactory.getTokenServices(OAuth2AccessToken.class);
    OAuth2AccessToken accessToken = tokenServices.createAccessToken(auth2);
    loginUser.setAttribute("token", accessToken);
    publisher.publishEvent(new LoginSuccessEvent(authentication));
    return loginUser;
  }

  public Boolean bindThirdPartyPlatform(String authCode, SocialProvider platform) {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    if (loginUser == null) {
      throw new UnauthorizedException("没有登录时，无法绑定第三方平台");
    }
    return Boolean.TRUE;
  }

  public Boolean logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!authentication.isAuthenticated()) {
      return Boolean.FALSE;
    }
    BearerTokenAuthentication tokenAuthentication = (BearerTokenAuthentication) authentication;
    ConsumerTokenServices consumerTokenServices =
        tokenServiceFactory.getTokenServices(OAuth2AccessToken.class);
    consumerTokenServices.revokeToken(tokenAuthentication.getToken().getTokenValue());
    publisher.publishEvent(new LogoutSuccessEvent(authentication));
    return Boolean.TRUE;
  }

  private Authentication buildAuthenticationToken(
      LoginType loginType,
      String username,
      String password,
      String authCode,
      String tmpAuthCode,
      LoginOptions options,
      AuthGraphQLServletContext context) {

    options = ObjectUtil.defaultValue(options, LoginOptions.builder().build());
    LoginAuthenticationDetails details =
        new LoginAuthenticationDetails(loginType, options, context.getRequest());
    AbstractAuthenticationToken authentication =
        switch (loginType) {
          case password -> new UsernamePasswordAuthenticationToken(username, password);
          case dingtalk -> new DingtalkAuthenticationToken(
              DingtalkAuthenticationToken.DingtalkCredentials.builder()
                  .authCode(authCode)
                  .connected(options.getConnected())
                  .build());
          case WeChatCP, WeChatPM -> new WeChatAuthenticationToken(
              WeChatAuthenticationToken.WeChatCredentials
                  .builder() /*.type(Oauth2.MobileType.WeChatCP)*/
                  .authCode(authCode)
                  .connected(options.getConnected())
                  .build());
            /*.type(Oauth2.MobileType.WeChatPM)*/
          default -> throw new IllegalStateException(
              "Unexpected AuthenticationToken By: " + loginType);
        };

    authentication.setDetails(details);

    return authentication;
  }
}
