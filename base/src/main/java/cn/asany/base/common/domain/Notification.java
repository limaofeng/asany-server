package cn.asany.base.common.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 通知
 *
 * @author limaofeng
 */
@Data
public class Notification implements Serializable {

  //  标题（Title）：通知的标题，用于概括通知内容。
  //  内容（Content）：通知的具体内容，包含详细的信息。
  //  发送人（Sender）：发送通知的人或系统的名称或标识。
  //  接收人（Recipient）：接收通知的人或系统的名称或标识。
  //  时间戳（Timestamp）：通知的发送时间或接收时间。
  //  类型（Type）：通知的类型，用于区分不同种类的通知，例如提醒、警告、通知等。
  //  优先级（Priority）：通知的重要程度或紧急程度。
  //  链接（Link）：通知相关的链接，例如点击链接可以跳转到相关页面或执行相关操作。
  //  状态（Status）：通知的状态，例如已发送、未读、已读等。

  /** 通知ID */
  private String id;
  /** 通知标题 */
  private String title;
  /** 通知内容 */
  private String content;
  /** 发送人 */
  private String sender;
  /** 接收人 */
  private String recipient;
  /** 时间戳 */
  private String timestamp;
  /** 类型 */
  private String type;
  /** 优先级 */
  private String priority;
  /** 链接 */
  private String link;
  /** 状态 */
  private String status;
  /** 租户ID */
  private String tenantId;
}
