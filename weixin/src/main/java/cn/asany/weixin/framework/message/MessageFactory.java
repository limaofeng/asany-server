package cn.asany.weixin.framework.message;

import cn.asany.storage.api.FileObject;
import cn.asany.weixin.framework.core.WeixinService;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.factory.WeixinSessionUtils;
import cn.asany.weixin.framework.message.content.*;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.cglib.CglibUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;

/** 微信消息工厂 */
@Slf4j
public class MessageFactory {

  /**
   * 文本消息
   *
   * @param msgId 消息id
   * @param fromUserName 发送方帐号（一个OpenID）
   * @param createTime 消息创建时间
   * @param content 文本消息内容
   */
  public static TextMessage createTextMessage(
      Long msgId, String fromUserName, Date createTime, String content) throws WeixinException {
    TextMessage message = new TextMessage(msgId, fromUserName, createTime);
    message.setToUserName(WeixinSessionUtils.getCurrentSession().getWeixinApp().getPrimitiveId());
    message.setContent(content);
    return message;
  }

  /**
   * 图片消息
   *
   * @param msgId 消息id
   * @param fromUserName 发送方帐号（一个OpenID）
   * @param createTime 消息创建时间
   * @param mediaId 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
   * @param url 图片链接
   * @return ImageMessage
   */
  public static ImageMessage createImageMessage(
      final WeixinService weiXinService,
      Long msgId,
      String fromUserName,
      Date createTime,
      String mediaId,
      String url)
      throws WeixinException {
    ImageMessage message = new ImageMessage(msgId, fromUserName, createTime);
    message.setToUserName(WeixinSessionUtils.getCurrentSession().getWeixinApp().getPrimitiveId());
    Media media =
        CglibUtil.newInstance(
            Media.class,
            (o, method, objects, methodProxy) -> {
              try {
                if ("getFileItem".equalsIgnoreCase(method.getName())) {
                  FileObject fileItem = (FileObject) methodProxy.invokeSuper(o, objects);
                  org.jfantasy.framework.util.reflect.MethodProxy _methodProxy =
                      ClassUtil.getMethodProxy(Media.class, "getId");
                  if (_methodProxy == null) {
                    return null;
                  }
                  String id = (String) _methodProxy.invoke(o);
                  if (fileItem == null && StringUtil.isNotBlank(id)) {
                    _methodProxy =
                        ClassUtil.getMethodProxy(Media.class, "setFileItem", FileObject.class);
                    if (_methodProxy == null) {
                      return null;
                    }
                    _methodProxy.invoke(o, fileItem = weiXinService.mediaDownload(id));
                  }
                  return fileItem;
                } else {
                  return methodProxy.invokeSuper(o, objects);
                }
              } catch (Throwable throwable) {
                log.error(throwable.getMessage());
                return null;
              }
            });
    media.setId(mediaId);
    Image image = new Image(media, url);
    message.setContent(image);

    return message;
  }

  /**
   * 语音消息
   *
   * @param msgId 消息id
   * @param fromUserName 发送方帐号（一个OpenID）
   * @param createTime 消息创建时间
   * @param mediaId 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
   * @param format 语音格式，如amr，speex等
   * @return VoiceMessage
   */
  public static VoiceMessage createVoiceMessage(
      Long msgId,
      String fromUserName,
      Date createTime,
      String mediaId,
      String format,
      String recognition)
      throws WeixinException {
    VoiceMessage message = new VoiceMessage(msgId, fromUserName, createTime);
    message.setToUserName(WeixinSessionUtils.getCurrentSession().getWeixinApp().getPrimitiveId());
    message.setContent(new Voice(new Media(mediaId, format), recognition));
    return message;
  }

  /**
   * 视频消息
   *
   * @param msgId 消息id
   * @param fromUserName 发送方帐号（一个OpenID）
   * @param createTime 消息创建时间
   * @param mediaId 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
   * @param thumbMediaId 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
   * @return VideoMessage
   */
  public static VideoMessage createVideoMessage(
      Long msgId, String fromUserName, Date createTime, String mediaId, String thumbMediaId)
      throws WeixinException {
    VideoMessage message = new VideoMessage(msgId, fromUserName, createTime);
    message.setToUserName(WeixinSessionUtils.getCurrentSession().getWeixinApp().getPrimitiveId());
    Video video = new Video(new Media(mediaId), new Media(thumbMediaId));
    message.setContent(video);
    return message;
  }

  /**
   * 地理位置消息
   *
   * @param msgId 消息id
   * @param fromUserName 发送方帐号（一个OpenID）
   * @param createTime 消息创建时间
   * @param x 地理位置维度
   * @param y 地理位置经度
   * @param scale 地图缩放大小
   * @param label 地理位置信息
   * @return LocationMessage
   */
  public static LocationMessage createLocationMessage(
      Long msgId,
      String fromUserName,
      Date createTime,
      Double x,
      Double y,
      Double scale,
      String label)
      throws WeixinException {
    LocationMessage message = new LocationMessage(msgId, fromUserName, createTime);
    message.setToUserName(WeixinSessionUtils.getCurrentSession().getWeixinApp().getPrimitiveId());
    message.setContent(new Location(x, y, scale, label));
    return message;
  }

  /**
   * 链接消息
   *
   * @param msgId 消息id
   * @param fromUserName 发送方帐号（一个OpenID）
   * @param createTime 消息创建时间
   * @param title 消息标题
   * @param description 消息描述
   * @param url 消息链接
   * @return LinkMessage
   */
  public static LinkMessage createLinkMessage(
      Long msgId,
      String fromUserName,
      Date createTime,
      String title,
      String description,
      String url)
      throws WeixinException {
    LinkMessage message = new LinkMessage(msgId, fromUserName, createTime);
    message.setToUserName(WeixinSessionUtils.getCurrentSession().getWeixinApp().getPrimitiveId());
    message.setContent(new Link(title, description, url));
    return message;
  }

  public static WeixinMessage createEventMessage(
      Long msgId,
      String fromUserName,
      Date createTime,
      String event,
      String eventKey,
      String ticket,
      Double latitude,
      Double longitude,
      Double precision)
      throws WeixinException {
    DefalutEventMessage message = new DefalutEventMessage(msgId, fromUserName, createTime);
    message.setToUserName(WeixinSessionUtils.getCurrentSession().getWeixinApp().getPrimitiveId());
    if (latitude != null && longitude != null && precision != null) {
      message.setContent(new EventLocation(event, latitude, longitude, precision));
    } else if (eventKey != null && ticket != null) {
      message.setContent(new Event(event, eventKey, ticket));
    } else if (eventKey != null) {
      message.setContent(new Event(event, eventKey));
    } else {
      message.setContent(new Event(event));
    }
    return message;
  }
}
