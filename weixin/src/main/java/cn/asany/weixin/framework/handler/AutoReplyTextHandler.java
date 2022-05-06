package cn.asany.weixin.framework.handler;

import cn.asany.weixin.framework.message.EmptyMessage;
import cn.asany.weixin.framework.message.TextMessage;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 自动回复处理器 */
public class AutoReplyTextHandler extends TextWeixinHandler {

  private static final Log LOG = LogFactory.getLog(AutoReplyTextHandler.class);

  private List<AutoReplyHandler> handlers = new ArrayList<AutoReplyHandler>();

  @Override
  protected WeixinMessage handleTextMessage(WeixinSession session, TextMessage message) {
    String keyword = message.getContent();
    for (AutoReplyHandler handler : handlers) {
      if (handler.handle(keyword)) {
        LOG.debug(keyword + " => " + handler);
        return handler.autoReply(keyword);
      }
    }
    return new EmptyMessage();
  }

  public void setHandlers(List<AutoReplyHandler> handlers) {
    this.handlers = handlers;
  }
}
