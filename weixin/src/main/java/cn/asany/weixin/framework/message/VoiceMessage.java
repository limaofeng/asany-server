package cn.asany.weixin.framework.message;

import cn.asany.weixin.framework.message.content.Voice;
import java.util.Date;

/** 微信语言消息 */
public class VoiceMessage extends AbstractWeixinMessage<Voice> {

  public VoiceMessage(Long id, String fromUserName, Date createTime) {
    super(id, fromUserName, createTime);
  }

  public VoiceMessage(Voice content) {
    super(content);
  }
}
