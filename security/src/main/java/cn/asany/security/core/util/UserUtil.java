package cn.asany.security.core.util;

import cn.asany.base.common.domain.enums.EmailStatus;
import cn.asany.base.common.domain.enums.PhoneNumberStatus;
import cn.asany.security.core.domain.User;
import net.asany.jfantasy.framework.security.LoginUser;

public class UserUtil {

  public static final String LOGIN_ATTRS_AVATAR = "_avatar";

  public static LoginUser buildLoginUser(User user) {
    LoginUser.LoginUserBuilder builder =
        LoginUser.builder()
            .uid(user.getId())
            .type(user.getUserType().name())
            .username(user.getUsername())
            .title(user.getTitle())
            .name(user.getNickname())
            .password(user.getPassword())
            .enabled(user.getEnabled())
            .accountNonExpired(user.getAccountNonExpired())
            .accountNonLocked(user.getAccountNonLocked())
            .credentialsNonExpired(user.getCredentialsNonExpired())
            .authorities(user.getAuthorities())
            .tenantId(user.getTenantId());

    if (user.getPhone() != null && user.getPhone().getStatus() == PhoneNumberStatus.VERIFIED) {
      builder.phone(user.getPhone().getNumber());
    }

    if (user.getEmail() != null && user.getEmail().getStatus() == EmailStatus.VERIFIED) {
      builder.phone(user.getEmail().getAddress());
    }

    if (user.getAvatar() != null) {
      builder.avatar(user.getAvatar().getPath());
    }

    LoginUser loginUser = builder.build();
    loginUser.setAttribute(LOGIN_ATTRS_AVATAR, user.getAvatar());
    return loginUser;
  }
}
