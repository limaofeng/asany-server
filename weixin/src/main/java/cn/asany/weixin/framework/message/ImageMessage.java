package cn.asany.weixin.framework.message;

import cn.asany.weixin.framework.message.content.Image;
import java.util.Date;

/** 图片消息 */
public class ImageMessage extends AbstractWeixinMessage<Image> {

  public ImageMessage(Long id, String fromUserName, Date createTime) {
    super(id, fromUserName, createTime);
  }

  public ImageMessage(Image content) {
    super(content);
  }
}
