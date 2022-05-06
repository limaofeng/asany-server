package cn.asany.weixin.framework.core;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.message.content.*;
import cn.asany.weixin.framework.message.user.OpenIdList;
import cn.asany.weixin.framework.message.user.User;
import cn.asany.weixin.framework.session.WeixinApp;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/** 微信签名相关接口 */
public interface WeixinCoreHelper {

  /**
   * 注册公众号服务，如果账号信息有更改需要重新调用该方法
   *
   * @param weixinApp 账号信息
   */
  void register(WeixinApp weixinApp);

  /**
   * 解析接收到的消息
   *
   * @param session 微信号session对象
   * @param request HTTP请求
   * @return WeiXinMessage
   * @throws WeixinException
   */
  WeixinMessage parseInMessage(WeixinSession session, HttpServletRequest request)
      throws WeixinException;

  /**
   * 构建回复的消息
   *
   * @param session 微信号session对象
   * @param encryptType encryptType
   * @param message 消息
   * @return String
   * @throws WeixinException
   */
  String buildOutMessage(WeixinSession session, String encryptType, WeixinMessage message)
      throws WeixinException;

  /**
   * 发送图片消息
   *
   * @param session 微信号session对象
   * @param content 图片消息
   * @param toUsers 接收人
   * @throws WeixinException
   */
  void sendImageMessage(WeixinSession session, Image content, String... toUsers)
      throws WeixinException;

  /**
   * 发送图片消息
   *
   * @param session 微信号session对象
   * @param content 图片消息
   * @param toGroup 接收组
   * @throws WeixinException
   */
  void sendImageMessage(WeixinSession session, Image content, long toGroup) throws WeixinException;

  /**
   * 发送语音消息
   *
   * @param session 微信号session对象
   * @param content 语音消息
   * @param toUsers 接收人
   * @throws WeixinException
   */
  void sendVoiceMessage(WeixinSession session, Voice content, String... toUsers)
      throws WeixinException;

  /**
   * 发送语音消息
   *
   * @param session 微信号session对象
   * @param content 语音消息
   * @param toGroup 接收人
   * @throws WeixinException
   */
  void sendVoiceMessage(WeixinSession session, Voice content, long toGroup) throws WeixinException;

  /**
   * 发送视频消息
   *
   * @param session 微信号session对象
   * @param content 视频消息
   * @param toUsers 接收人
   * @throws WeixinException
   */
  void sendVideoMessage(WeixinSession session, Video content, String... toUsers)
      throws WeixinException;

  /**
   * 发送视频消息
   *
   * @param session 微信号session对象
   * @param content 视频消息
   * @param toGroup 接收人
   * @throws WeixinException
   */
  void sendVideoMessage(WeixinSession session, Video content, long toGroup) throws WeixinException;

  /**
   * 发送音乐消息
   *
   * @param session 微信号session对象
   * @param content 音乐消息
   * @param toUser 接收人
   * @throws WeixinException
   */
  void sendMusicMessage(WeixinSession session, Music content, String toUser) throws WeixinException;

  /**
   * 发送音乐消息
   *
   * @param session 微信号session对象
   * @param content 图文消息
   * @param toUser 接收人
   * @throws WeixinException
   */
  void sendNewsMessage(WeixinSession session, List<News> content, String toUser)
      throws WeixinException;

  /**
   * 发送图文消息
   *
   * @param session 微信号session对象
   * @param articles 图文消息
   * @param toUsers 接收人
   * @throws WeixinException
   */
  void sendNewsMessage(WeixinSession session, List<Article> articles, String... toUsers)
      throws WeixinException;

  /**
   * 发送图文消息
   *
   * @param session 微信号session对象
   * @param articles 图文消息
   * @param toGroup 接收人
   * @throws WeixinException
   */
  void sendNewsMessage(WeixinSession session, List<Article> articles, long toGroup)
      throws WeixinException;

  /**
   * 发送文本消息
   *
   * @param session 微信号session对象
   * @param content 文本消息
   * @param toUsers 接收人
   * @throws WeixinException
   */
  void sendTextMessage(WeixinSession session, String content, String... toUsers)
      throws WeixinException;

  /**
   * 发送文本消息
   *
   * @param session 微信号session对象
   * @param content 文本消息
   * @param toGroup 接收组
   * @throws WeixinException
   */
  void sendTextMessage(WeixinSession session, String content, long toGroup) throws WeixinException;

  /**
   * 发送模板消息
   *
   * @param session 微信号session对象
   * @param content 模板消息
   * @param toUser 接收人
   * @throws WeixinException
   */
  void sendTemplateMessage(WeixinSession session, Template content, String toUser)
      throws WeixinException;

  /**
   * 获取全部用户关注用户 <br>
   * 该方法仅在微信粉丝数量有限的情况下，推荐使用
   *
   * @param session 微信号session对象
   * @return List<User>
   */
  List<User> getUsers(WeixinSession session) throws WeixinException;

  /**
   * 公众号可通过本接口来获取帐号的关注者列表
   *
   * @param session 微信号session对象
   * @return UserList
   */
  OpenIdList getOpenIds(WeixinSession session) throws WeixinException;

  /**
   * 公众号可通过本接口来获取帐号的关注者列表
   *
   * @param session 微信号session对象
   * @param nextOpenId 第一个拉取的OPENID，不填默认从头开始拉取
   * @return UserList
   */
  OpenIdList getOpenIds(WeixinSession session, String nextOpenId) throws WeixinException;

  /**
   * 获取全部用户关注用户
   *
   * @param session 微信号session对象
   * @param userId 用户id
   * @return List<User>
   */
  User getUser(WeixinSession session, String userId) throws WeixinException;

  /**
   * 媒体上传接口
   *
   * @param session 微信号session对象
   * @param mediaType 媒体类型
   * @param fileItem 要上传的文件
   * @return 媒体Id
   */
  String mediaUpload(WeixinSession session, Media.Type mediaType, Object fileItem)
      throws WeixinException;

  /**
   * 媒体下载接口
   *
   * @param session 微信号session对象
   * @param mediaId 媒体id
   * @return FileItem
   * @throws WeixinException
   */
  Object mediaDownload(WeixinSession session, String mediaId) throws WeixinException;

  /**
   * 获取安全链接
   *
   * @param session 微信号session对象
   * @param redirectUri 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
   * @param scope 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo
   *     （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
   * @param state 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值
   * @return url
   * @throws WeixinException String oauth2buildAuthorizationUrl(WeixinSession session, String
   *     redirectUri, Scope scope, String state) throws WeixinException;
   */

  /**
   * 刷新菜单配置
   *
   * @param session 微信号session对象
   * @param menus 菜单数组
   * @throws WeixinException
   */
  void refreshMenu(WeixinSession session, Menu... menus) throws WeixinException;

  /**
   * 获取配置的菜单
   *
   * @param session 微信号session对象
   * @return List<Menu>
   * @throws WeixinException
   */
  List<Menu> getMenus(WeixinSession session) throws WeixinException;

  /**
   * 清除Menu配置
   *
   * @param session 微信号session对象
   * @throws WeixinException
   */
  void clearMenu(WeixinSession session) throws WeixinException;

  /**
   * 获取jsapi
   *
   * @param session 微信号session对象
   * @return Jsapi
   * @throws WeixinException
   */
  Jsapi getJsapi(WeixinSession session) throws WeixinException;

  Openapi getOpenapi(WeixinSession session) throws WeixinException;
}
