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
package cn.asany.security.core.convert;

import cn.asany.base.common.domain.Email;
import cn.asany.base.common.domain.Phone;
import cn.asany.security.auth.graphql.types.CurrentUser;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.graphql.input.UserCreateInput;
import cn.asany.security.core.graphql.input.UserUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserConverter {

  /**
   * 新增用户
   *
   * @param input 输入
   * @return 菜单
   */
  @Mappings({
    @Mapping(target = "phone", qualifiedByName = "toPhone"),
    @Mapping(target = "email", qualifiedByName = "toEmail")
  })
  User toUser(UserCreateInput input);

  /**
   * 更新用户
   *
   * @param input 输入
   * @return 菜单
   */
  @Mappings({
    @Mapping(target = "phone", qualifiedByName = "toPhone"),
    @Mapping(target = "email", qualifiedByName = "toEmail")
  })
  User toUser(UserUpdateInput input);

  @Mappings({})
  CurrentUser toCurrentUser(User user);

  @Named("toPhone")
  default Phone toPhone(String source) {
    if (source == null) {
      return null;
    }
    return Phone.builder().number(source).build();
  }

  @Named("toEmail")
  default Email toEmail(String source) {
    if (source == null) {
      return null;
    }
    return Email.builder().address(source).build();
  }
}
