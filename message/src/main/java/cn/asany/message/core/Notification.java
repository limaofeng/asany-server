package cn.asany.message.core;

import lombok.Builder;
import lombok.Data;

/**
 * 通知对象
 *
 * @author limaofeng
 */
@Data
@Builder
public class Notification {
  private String title;
  private String content;
}
