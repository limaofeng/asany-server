package cn.asany.weixin.framework.handler;

import cn.asany.weixin.framework.message.WeixinMessage;

public class AutoReplyHandler {

  public boolean handle(String keyword) {
    return false;
  }

  public WeixinMessage autoReply(String keyword) {
    return null;
  }
}
