package cn.asany.weixin.framework.message;

import cn.asany.weixin.framework.message.content.Link;
import java.util.Date;

/** 链接消息 */
public class LinkMessage extends AbstractWeixinMessage<Link> {

  public LinkMessage(Long id, String fromUserName, Date createTime) {
    super(id, fromUserName, createTime);
  }
}
