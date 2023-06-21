package cn.asany.message.api;

import java.util.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * 简单消息
 *
 * @author limaofeng
 */
@Data
@Builder
public class SimpleMessage {
  /** 消息发送者 */
  private String from;
  /** 消息接收者 */
  private String[] to;
  /** 消息发送时间 */
  private Date sentDate;
  /** 消息主题 */
  private String subject;
  /** 消息内容 */
  private String text;
  /** 签名名称 */
  private String signName;
  /** 模板代码 */
  private String templateCode;
  /** 模板参数 */
  private Map<String, String> templateParams;
}
