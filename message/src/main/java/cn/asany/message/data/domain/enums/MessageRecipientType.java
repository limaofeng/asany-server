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
package cn.asany.message.data.domain.enums;

import net.asany.jfantasy.framework.error.ValidationException;

/**
 * 消息收件人类型
 *
 * @author limaofeng
 */
public enum MessageRecipientType {
  /** 用户 */
  USER("user"),
  /** 群组 */
  GROUP("group"),
  /** 部门 */
  DEPARTMENT("department"),
  /** 手机 */
  PHONE("phone"),
  /** 邮箱 */
  EMAIL("email");

  public static final String DELIMITER = ":";

  private final String name;

  MessageRecipientType(String name) {
    this.name = name;
  }

  public static MessageRecipientType of(String value) {
    if (value.startsWith(USER.getName() + DELIMITER)) {
      return USER;
    } else if (value.startsWith(GROUP.getName() + DELIMITER)) {
      return GROUP;
    } else if (value.startsWith(DEPARTMENT.getName() + DELIMITER)) {
      return DEPARTMENT;
    } else if (value.startsWith(PHONE.getName() + DELIMITER)) {
      return PHONE;
    } else if (value.startsWith(EMAIL.getName() + DELIMITER)) {
      return EMAIL;
    }
    throw new ValidationException("收件人格式错误：" + value);
  }

  public String getName() {
    return name;
  }
}
