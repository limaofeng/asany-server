package cn.asany.message.core;

import cn.asany.message.define.domain.MessageType;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * 通知对象
 *
 * @author limaofeng
 */
@Data
@Builder
public class Notification implements Serializable {
  private Long id;
  /** 类型 */
  private MessageType type;
  /** 系统消息 */
  private boolean systemMessage;
  /** 用户ID */
  private Long userId;
  /** 标题 */
  private String title;
  /** 消息 */
  private String message;
  /** URI */
  private String uri;
}
