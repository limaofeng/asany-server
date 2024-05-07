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
            .phone(user.getPhone().getNumber())
            .email(user.getEmail().getAddress())
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
