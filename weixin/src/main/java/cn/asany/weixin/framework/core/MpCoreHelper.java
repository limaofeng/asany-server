package cn.asany.weixin.framework.core;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.message.content.*;
import cn.asany.weixin.framework.message.user.OpenIdList;
import cn.asany.weixin.framework.message.user.User;
import cn.asany.weixin.framework.session.WeixinApp;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/** 微信服务号与订阅号 */
@Component
public class MpCoreHelper implements WeixinCoreHelper {

  private Map<String, WeixinDetails> weiXinDetailsMap = new HashMap<String, WeixinDetails>();

  @Override
  public void register(WeixinApp weixinApp) {
    if (weiXinDetailsMap.containsKey(weixinApp.getId())) {
      weiXinDetailsMap.remove(weixinApp.getId());
    }
    weiXinDetailsMap.put(weixinApp.getId(), new WeixinDetails(weixinApp));
  }

  @Override
  public WeixinMessage parseInMessage(WeixinSession session, HttpServletRequest request)
      throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().parseInMessage(request);
  }

  @Override
  public String buildOutMessage(WeixinSession session, String encryptType, WeixinMessage message)
      throws WeixinException {
    return getWeiXinDetails(session.getId())
        .getWeixinService()
        .parseInMessage(encryptType, message);
  }

  @Override
  public void sendImageMessage(WeixinSession session, Image content, String... toUsers)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendImageMessage(content, toUsers);
  }

  @Override
  public void sendImageMessage(WeixinSession session, Image content, long toGroup)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendImageMessage(content, toGroup);
  }

  @Override
  public void sendVoiceMessage(WeixinSession session, Voice content, String... toUsers)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendVoiceMessage(content, toUsers);
  }

  @Override
  public void sendVoiceMessage(WeixinSession session, Voice content, long toGroup)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendVoiceMessage(content, toGroup);
  }

  @Override
  public void sendVideoMessage(WeixinSession session, Video content, String... toUsers)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendVideoMessage(content, toUsers);
  }

  @Override
  public void sendVideoMessage(WeixinSession session, Video content, long toGroup)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendVideoMessage(content, toGroup);
  }

  @Override
  public void sendMusicMessage(WeixinSession session, Music content, String toUser)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendMusicMessage(content, toUser);
  }

  @Override
  public void sendNewsMessage(WeixinSession session, List<News> content, String toUser)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendNewsMessage(content, toUser);
  }

  public void sendNewsMessage(WeixinSession session, List<Article> articles, String... toUsers)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendNewsMessage(articles, toUsers);
  }

  @Override
  public void sendNewsMessage(WeixinSession session, List<Article> articles, long toGroup)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendNewsMessage(articles, toGroup);
  }

  @Override
  public void sendTextMessage(WeixinSession session, String content, String... toUsers)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendTextMessage(content, toUsers);
  }

  @Override
  public void sendTextMessage(WeixinSession session, String content, long toGroup)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendTextMessage(content, toGroup);
  }

  @Override
  public void sendTemplateMessage(WeixinSession session, Template content, String toUser)
      throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().sendTemplateMessage(content, toUser);
  }

  @Override
  public List<User> getUsers(WeixinSession session) throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().getUsers();
  }

  @Override
  public OpenIdList getOpenIds(WeixinSession session) throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().getOpenIds();
  }

  @Override
  public OpenIdList getOpenIds(WeixinSession session, String nextOpenId) throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().getOpenIds(nextOpenId);
  }

  @Override
  public User getUser(WeixinSession session, String userId) throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().getUser(userId);
  }

  @Override
  public String mediaUpload(WeixinSession session, Media.Type mediaType, Object fileItem)
      throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().mediaUpload(mediaType, fileItem);
  }

  public Object mediaDownload(WeixinSession session, String mediaId) throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().mediaDownload(mediaId);
  }

  @Override
  public void refreshMenu(WeixinSession session, Menu... menus) throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().refreshMenu(menus);
  }

  public Jsapi getJsapi(WeixinSession session) throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().getJsapi();
  }

  public Openapi getOpenapi(WeixinSession session) throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().getOpenapi();
  }

  @Override
  public List<Menu> getMenus(WeixinSession session) throws WeixinException {
    return getWeiXinDetails(session.getId()).getWeixinService().getMenus();
  }

  @Override
  public void clearMenu(WeixinSession session) throws WeixinException {
    getWeiXinDetails(session.getId()).getWeixinService().clearMenu();
  }

  private WeixinDetails getWeiXinDetails(String appid) throws WeixinException {
    if (!weiXinDetailsMap.containsKey(appid)) {
      throw new WeixinException("[appid=" + appid + "]未注册！");
    }
    return weiXinDetailsMap.get(appid);
  }
}
