package cn.asany.message.api;

import java.util.Date;
import java.util.Map;
import lombok.Data;

/**
 * 简单消息
 *
 * @author limaofeng
 */
@Data
public class SimpleMessage {
  private String from;
  private String[] to;
  private Date sentDate;
  private String subject;
  private String text;
  private String signName;
  private String templateCode;
  private Map<String, String> templateParams;
}
