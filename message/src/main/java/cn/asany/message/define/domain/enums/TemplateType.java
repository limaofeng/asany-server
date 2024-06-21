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
package cn.asany.message.define.domain.enums;

import cn.asany.message.api.enums.MessageRecipientType;

/**
 * 模版类型
 *
 * @author limaofeng
 */
public enum TemplateType {
  /** 短信 */
  SMS(MessageRecipientType.PHONE),
  /** 邮件 */
  EMAIL(MessageRecipientType.EMAIL),
  /** 消息服务(系统内置消息系统) */
  MS(MessageRecipientType.USER);

  private final MessageRecipientType recipientType;

  TemplateType(MessageRecipientType r) {
    this.recipientType = r;
  }

  public MessageRecipientType getRecipientType() {
    return recipientType;
  }
}
