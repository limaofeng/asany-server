package cn.asany.security.core.graphql;

import cn.asany.base.common.domain.Phone;
import cn.asany.base.common.domain.enums.PhoneNumberStatus;
import cn.asany.security.core.convert.UserConverter;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.domain.enums.UserType;
import cn.asany.security.core.graphql.input.UserUpdateInput;
import cn.asany.security.core.service.UserService;
import cn.asany.storage.api.FileObject;
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

  /**
   * @param nickName 昵称
   * @param avatar 头像
   * @param phoneNumber 电话号码
   * @param password 密码
   * @param smsCode 短信验证码
   * @return User
   */
  public LoginUser register(
      String nickName, FileObject avatar, String phoneNumber, String password, String smsCode) {

    User user =
        User.builder()
            .nickName(nickName)
            .avatar(avatar)
            .userType(UserType.USER)
            .username(phoneNumber)
            .password(password)
            .phone(Phone.builder().number(phoneNumber).status(PhoneNumberStatus.VERIFIED).build())
            .build();

    return this.userService.register(user);
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
