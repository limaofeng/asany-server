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
package cn.asany.security.core.graphql.input;

import cn.asany.security.core.domain.enums.Sex;
import cn.asany.storage.api.FileObject;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateInput {
  /** 昵称 */
  private String nickname;

  /** 头像 */
  private FileObject avatar;

  /** 电话 */
  private String phone;

  /** 邮箱 */
  private String email;

  /** 生日 */
  private Date birthday;

  /** 性别 */
  private Sex sex;

  /** 自我介绍 */
  private String bio;

  /** 公司 */
  private String company;

  /** 位置 */
  private String location;

  /** 密码 */
  private String password;
}
