package cn.asany.weixin.framework.session;

import cn.asany.weixin.framework.core.Jsapi;
import cn.asany.weixin.framework.core.Openapi;
import cn.asany.weixin.framework.message.content.*;
import cn.asany.weixin.framework.message.user.User;
import java.util.List;

/** 微信 session 接口 主要包含微信消息相关的公众号及订阅号内容 */
public interface WeixinSession {

  /**
   * 微信号的 appid
   *
   * @return String
   */
  String getId();

  /**
   * 发送图片消息
   *
   * @param content 图片消息
   * @param toUsers 接收人
   */
  void sendImageMessage(Image content, String... toUsers);

  /**
   * 发送图片消息
   *
   * @param content 图片消息
   * @param toGroup 接收组
   */
  void sendImageMessage(Image content, long toGroup);

  /**
   * 发送语音消息
   *
   * @param content 语音消息
   * @param toUsers 接收人
   */
  void sendVoiceMessage(Voice content, String... toUsers);

  /**
   * 发送语音消息
   *
   * @param content 语音消息
   * @param toGroup 接收人
   */
  void sendVoiceMessage(Voice content, long toGroup);

  /**
   * 发送视频消息
   *
   * @param content 视频消息
   * @param toUsers 接收人
   */
  void sendVideoMessage(Video content, String... toUsers);

  /**
   * 发送视频消息
   *
   * @param content 视频消息
   * @param toGroup 接收组
   */
  void sendVideoMessage(Video content, long toGroup);

  /**
   * 发送音乐消息
   *
   * @param content 音乐消息
   * @param toUser 接收人
   */
  void sendMusicMessage(Music content, String toUser);

  /**
   * 发送图文消息
   *
   * @param content 图文消息
   * @param toUser 接收人
   */
  void sendNewsMessage(List<News> content, String toUser);

  /**
   * 发送图文消息
   *
   * @param content 图文消息列表
   * @param toUsers 接收人
   */
  void sendNewsMessage(List<Article> content, String... toUsers);

  /**
   * 发送图文消息
   *
   * @param content 图文消息列表
   * @param toGroup 接收人
   */
  void sendNewsMessage(List<Article> content, long toGroup);

  /**
   * 发送文本消息
   *
   * @param content 文本消息
   * @param toUsers 接收人
   */
  void sendTextMessage(String content, String... toUsers);

  /**
   * 发送文本消息
   *
   * @param content 文本消息
   * @param toGroup 接收组
   */
  void sendTextMessage(String content, long toGroup);

  /**
   * 发送模板消息
   *
   * @param content 模板消息
   * @param toUser 接收人
   */
  void sendTemplateMessage(Template content, String toUser);

  /**
   * 获取安全连接的授权用户
   *
   * @param userId 关注粉丝的openId
   * @return User
   */
  User getUser(String userId);

  /**
   * 获取关注的粉丝
   *
   * @return List<User>
   */
  List<User> getUsers();

  /**
   * 获取当前公众号信息
   *
   * @return AccountDetails
   */
  WeixinApp getWeixinApp();

  /**
   * 刷新菜单配置
   *
   * @param menus 菜单数组
   */
  void refreshMenu(Menu... menus);

  /**
   * 获取配置的菜单
   *
   * @return List<Menu>
   */
  List<Menu> getMenus();

  /** 清除Menu配置 */
  void clearMenu();

  /**
   * 获取 jsapi
   *
   * @return Jsapi
   */
  Jsapi getJsapi();

  Openapi getOpenapi();
}
