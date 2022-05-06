package cn.asany.weixin.framework.message;

import java.util.Date;

/** 微信文本消息 */
public class TextMessage extends AbstractWeixinMessage<String> {

  public TextMessage(Long id, String fromUserName, Date createTime) {
    super(id, fromUserName, createTime);
  }

  public TextMessage(String content) {
    super(content);
  }
}
