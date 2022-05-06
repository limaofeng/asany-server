package cn.asany.weixin.framework.message;

import cn.asany.weixin.framework.message.content.Location;
import java.util.Date;

/** 地理位置消息 */
public class LocationMessage extends AbstractWeixinMessage<Location> {

  public LocationMessage(Long id, String fromUserName, Date createTime) {
    super(id, fromUserName, createTime);
  }
}
