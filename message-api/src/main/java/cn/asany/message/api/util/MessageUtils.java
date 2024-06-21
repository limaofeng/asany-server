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
package cn.asany.message.api.util;

import cn.asany.message.api.enums.MessageRecipientType;

public class MessageUtils {

  /**
   * 格式化收件人
   *
   * @param phone 手机号
   * @return 格式化后的收件人
   */
  public static String formatRecipientFromPhone(String phone) {
    return MessageRecipientType.PHONE.getName() + MessageRecipientType.DELIMITER + phone;
  }

  public static String formatRecipientFromUser(Long userId) {
    return MessageRecipientType.USER.getName() + MessageRecipientType.DELIMITER + userId;
  }

  /**
   * 格式化收件人
   *
   * @param email 邮箱
   * @return 格式化后的收件人
   */
  public static String formatRecipientFromEmail(String email) {
    return MessageRecipientType.EMAIL.getName() + MessageRecipientType.DELIMITER + email;
  }
}
