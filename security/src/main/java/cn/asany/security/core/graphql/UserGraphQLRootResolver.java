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
package cn.asany.security.core.graphql;

import cn.asany.base.common.BatchPayload;
import cn.asany.base.common.domain.Phone;
import cn.asany.base.common.domain.enums.PhoneNumberStatus;
import cn.asany.base.sms.CaptchaService;
import cn.asany.base.sms.CaptchaSource;
import cn.asany.security.auth.utils.AuthUtils;
import cn.asany.security.core.convert.UserConverter;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.domain.enums.UserType;
import cn.asany.security.core.event.RegisterSuccessEvent;
import cn.asany.security.core.graphql.input.UserCreateInput;
import cn.asany.security.core.graphql.input.UserSettingsInput;
import cn.asany.security.core.graphql.input.UserUpdateInput;
import cn.asany.security.core.graphql.input.UserWhereInput;
import cn.asany.security.core.graphql.types.UserConnection;
import cn.asany.security.core.service.UserService;
import cn.asany.storage.api.FileObject;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.LimitPageRequest;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import net.asany.jfantasy.framework.security.auth.core.token.AuthorizationServerTokenServices;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AccessToken;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2Authentication;
import net.asany.jfantasy.framework.security.authentication.SimpleAuthenticationToken;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.security.context.AuthGraphQLServletContext;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class UserGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final UserService userService;

  private final UserConverter userConverter;

  private final CaptchaService captchaService;

  private final ApplicationEventPublisher publisher;

  private final AuthorizationServerTokenServices<OAuth2AccessToken> tokenServices;

  public UserGraphQLRootResolver(
      UserService userService,
      UserConverter userConverter,
      CaptchaService captchaService,
      ApplicationEventPublisher publisher,
      AuthorizationServerTokenServices<OAuth2AccessToken> tokenServices) {
    this.userService = userService;
    this.userConverter = userConverter;
    this.captchaService = captchaService;
    this.publisher = publisher;
    this.tokenServices = tokenServices;
  }

  /**
   * 注册
   *
   * @param nickname 昵称
   * @param avatar 头像
   * @param phoneNumber 电话号码
   * @param password 密码
   * @param smsCode 短信验证码
   * @return User
   */
  public LoginUser register(
      String nickname,
      FileObject avatar,
      String phoneNumber,
      String password,
      String smsCode,
      DataFetchingEnvironment environment) {

    //noinspection deprecation
    AuthGraphQLServletContext context = environment.getContext();

    if (!this.captchaService.validateResponseForID(
        CaptchaSource.CAPTCHA_CONFIG_ID,
        CaptchaSource.getSessionId(phoneNumber, CaptchaSource.LOGIN),
        smsCode)) {
      return null;
    }

    User user =
        User.builder()
            .nickname(nickname)
            .avatar(avatar)
            .userType(UserType.USER)
            .username(phoneNumber)
            .password(password)
            .phone(Phone.builder().number(phoneNumber).status(PhoneNumberStatus.VERIFIED).build())
            .build();

    LoginUser loginUser = this.userService.register(user);

    SimpleAuthenticationToken<?> authentication = new SimpleAuthenticationToken<>(loginUser);
    authentication.setDetails(context.getAuthentication().getDetails());

    String clientId = context.getRequest().getHeader("X-Client-ID");

    OAuth2Authentication oauth2 =
        AuthUtils.buildOAuth2(clientId, context.getRequest(), authentication);

    OAuth2AccessToken accessToken = tokenServices.createAccessToken(oauth2);

    loginUser.setAttribute("token", accessToken);
    publisher.publishEvent(new RegisterSuccessEvent(loginUser));

    return loginUser;
  }

  /**
   * 查询所有用户,带条件查询
   *
   * @param where 查询条件
   * @param page 页码
   * @param pageSize 每页大小
   * @param orderBy 排序
   * @return UserConnection
   */
  public UserConnection usersConnection(
      UserWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    where = ObjectUtil.defaultValue(where, new UserWhereInput());
    return Kit.connection(userService.findPage(pageable, where.toFilter()), UserConnection.class);
  }

  /** 查询所有用户 - 列表 */
  public List<User> users(
      UserWhereInput where, int skip, int after, int before, int first, int last, Sort orderBy) {
    Pageable pageable = LimitPageRequest.of(skip, first, orderBy);
    PropertyFilter filter = ObjectUtil.defaultValue(where, new UserWhereInput()).toFilter();
    return userService.findPage(pageable, filter).getContent();
  }

  /**
   * 查询用户, ID 为空时查询当前用户
   *
   * @param id 用户ID
   * @return User
   */
  public User user(Long id) {
    Long uid =
        Optional.ofNullable(id).orElseGet(() -> SpringSecurityUtils.getCurrentUser().getUid());
    return this.userService.get(uid);
  }

  public User createUser(UserCreateInput input) {
    User user = this.userConverter.toUser(input);
    return this.userService.save(user);
  }

  public List<User> createManyUsers(List<UserCreateInput> users, UserSettingsInput settings) {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    return this.userService.saveAll(
        users.stream()
            .map(this.userConverter::toUser)
            .peek(
                user -> {
                  user.setPassword(settings.getPassword());
                  user.setForcePasswordReset(settings.isForcePasswordReset());
                  user.setTenantId(loginUser.getTenantId());
                })
            .collect(Collectors.toList()));
  }

  public User updateUser(Long id, UserUpdateInput input, Boolean merge) {
    User user = this.userConverter.toUser(input);
    return this.userService.update(id, user, merge);
  }

  public User deleteUser(Long id) {
    return this.userService.delete(id);
  }

  public BatchPayload deleteManyUsers(UserWhereInput where) {
    return BatchPayload.of(this.userService.deleteMany(where.toFilter()));
  }

  public Boolean changePassword(Long id, String oldPassword, String newPassword) {
    Long uid =
        Optional.ofNullable(id).orElseGet(() -> SpringSecurityUtils.getCurrentUser().getUid());
    userService.changePassword(uid, oldPassword, newPassword);
    return Boolean.TRUE;
  }
}
