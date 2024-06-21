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
package cn.asany.security.core.domain.enums;

import lombok.Getter;
import net.asany.jfantasy.framework.error.ValidationException;

/**
 * 被授权者类型
 *
 * @author limaofeng
 */
@Getter
public enum GranteeType {
  /** 用户 */
  USER("user"),
  /** 用户组 */
  GROUP("group"),
  /** 角色 */
  ROLE("role");

  public static final String DELIMITER = ":";

  private final String name;

  GranteeType(String name) {
    this.name = name;
  }

  public static GranteeType of(String value) {
    if (value.startsWith(USER.getName() + DELIMITER)) {
      return USER;
    } else if (value.startsWith(GROUP.getName() + DELIMITER)) {
      return GROUP;
    } else if (value.startsWith(ROLE.getName() + DELIMITER)) {
      return ROLE;
    }
    throw new ValidationException("被授权者类型格式错误：" + value);
  }
}
