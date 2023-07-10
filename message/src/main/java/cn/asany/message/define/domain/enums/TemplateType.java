package cn.asany.message.define.domain.enums;

import cn.asany.message.data.domain.enums.MessageRecipientType;

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
