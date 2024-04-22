package cn.asany.weixin.framework.core;

import cn.asany.storage.api.FileObject;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.*;
import cn.asany.weixin.framework.message.content.*;
import cn.asany.weixin.framework.message.user.OpenIdList;
import cn.asany.weixin.framework.message.user.User;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpConfigStorage;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.*;
import me.chanjar.weixin.cp.bean.messagebuilder.VideoBuilder;
import me.chanjar.weixin.cp.bean.outxmlbuilder.NewsBuilder;
import me.chanjar.weixin.cp.util.xml.XStreamTransformer;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

@Slf4j
public class WeixinCpService implements WeixinService {
  private final WxCpService wxCpService;
  private final WxCpConfigStorage wxCpConfigStorage;
  private final Jsapi jsapi;

  public WeixinCpService(WxCpService wxCpService, WxCpConfigStorage wxCpConfigStorage) {
    this.wxCpService = wxCpService;
    this.wxCpConfigStorage = wxCpConfigStorage;
    this.jsapi = new CpJsapi(this.wxCpService);
  }

  @Override
  public WeixinMessage<?> parseInMessage(HttpServletRequest request) throws WeixinException {
    String signature = request.getParameter("signature");
    String nonce = request.getParameter("nonce");
    String timestamp = request.getParameter("timestamp");
    String encrypt_type = request.getParameter("encrypt_type");
    String msg_signature = request.getParameter("msg_signature");
    InputStream input;
    try {
      input = request.getInputStream();
    } catch (IOException e) {
      throw new WeixinException(e.getMessage(), e);
    }

    if (!wxCpService.checkSignature(msg_signature, timestamp, nonce, signature)) {
      // 消息签名不正确，说明不是公众平台发过来的消息
      throw new WeixinException("非法请求");
    }

    String encryptType = StringUtils.isBlank(encrypt_type) ? "raw" : encrypt_type;

    WxCpXmlMessage inMessage;
    if ("raw".equals(encryptType)) {
      // 明文传输的消息
      inMessage = XStreamTransformer.fromXml(WxCpXmlMessage.class, input);
    } else if ("aes".equals(encryptType)) {
      // 是aes加密的消息
      inMessage =
          WxCpXmlMessage.fromEncryptedXml(
              input, wxCpConfigStorage, timestamp, nonce, msg_signature);
    } else {
      throw new WeixinException("不可识别的加密类型");
    }
    log.debug("inMessage=>" + JSON.serialize(inMessage));
    if ("text".equals(inMessage.getMsgType())) {
      return MessageFactory.createTextMessage(
          inMessage.getMsgId(),
          inMessage.getFromUserName(),
          new Date(inMessage.getCreateTime()),
          inMessage.getContent());
    } else if ("image".equalsIgnoreCase(inMessage.getMsgType())) {
      return MessageFactory.createImageMessage(
          this,
          inMessage.getMsgId(),
          inMessage.getFromUserName(),
          new Date(inMessage.getCreateTime()),
          inMessage.getMediaId(),
          inMessage.getUrl());
    } else if ("voice".equalsIgnoreCase(inMessage.getMsgType())) {
      return MessageFactory.createVoiceMessage(
          inMessage.getMsgId(),
          inMessage.getFromUserName(),
          new Date(inMessage.getCreateTime()),
          inMessage.getMediaId(),
          inMessage.getFormat(),
          inMessage.getRecognition());
    } else if ("video".equalsIgnoreCase(inMessage.getMsgType())) {
      return MessageFactory.createVideoMessage(
          inMessage.getMsgId(),
          inMessage.getFromUserName(),
          new Date(inMessage.getCreateTime()),
          inMessage.getMediaId(),
          inMessage.getThumbMediaId());
    } else if ("location".equalsIgnoreCase(inMessage.getMsgType())) {
      return MessageFactory.createLocationMessage(
          inMessage.getMsgId(),
          inMessage.getFromUserName(),
          new Date(inMessage.getCreateTime()),
          inMessage.getLocationX(),
          inMessage.getLocationY(),
          inMessage.getScale(),
          inMessage.getLabel());
    } else if ("link".equalsIgnoreCase(inMessage.getMsgType())) {
      return MessageFactory.createLinkMessage(
          inMessage.getMsgId(),
          inMessage.getFromUserName(),
          new Date(inMessage.getCreateTime()),
          inMessage.getTitle(),
          inMessage.getDescription(),
          inMessage.getUrl());
    } else if ("event".equalsIgnoreCase(inMessage.getMsgType())) {
      return MessageFactory.createEventMessage(
          inMessage.getMsgId(),
          inMessage.getFromUserName(),
          new Date(inMessage.getCreateTime()),
          inMessage.getEvent(),
          inMessage.getEventKey(),
          inMessage.getTicket(),
          inMessage.getLatitude(),
          inMessage.getLongitude(),
          inMessage.getPrecision());
    } else {
      log.debug(inMessage.toString());
      throw new WeixinException("无法处理的消息类型" + inMessage.getMsgType());
    }
  }

  @Override
  public String parseInMessage(String encryptType, WeixinMessage<?> message)
      throws WeixinException {
    WxCpXmlOutMessage outMessage;
    if (message instanceof TextMessage) {
      outMessage =
          WxCpXmlOutMessage.TEXT()
              .content(((TextMessage) message).getContent())
              .fromUser(message.getFromUserName())
              .toUser(message.getToUserName())
              .build();
    } else if (message instanceof ImageMessage) {
      Media media = ((ImageMessage) message).getContent().getMedia();
      media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
      outMessage =
          WxCpXmlOutMessage.IMAGE()
              .mediaId(media.getId())
              .fromUser(message.getFromUserName())
              .toUser(message.getToUserName())
              .build();
    } else if (message instanceof VoiceMessage) {
      Media media = ((VoiceMessage) message).getContent().getMedia();
      media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
      outMessage =
          WxCpXmlOutMessage.VOICE()
              .mediaId(media.getId())
              .fromUser(message.getFromUserName())
              .toUser(message.getToUserName())
              .build();
    } else if (message instanceof VideoMessage) {
      Media media = ((VideoMessage) message).getContent().getMedia();
      media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
      outMessage =
          WxCpXmlOutMessage.VIDEO()
              .mediaId(media.getId())
              .fromUser(message.getFromUserName())
              .toUser(message.getToUserName())
              .build();
    } else if (message instanceof NewsMessage) {
      List<News> newses = ((NewsMessage) message).getContent();
      NewsBuilder newsBuilder = WxCpXmlOutMessage.NEWS();
      for (News news : newses) {
        WxCpXmlOutNewsMessage.Item item = new WxCpXmlOutNewsMessage.Item();
        item.setTitle(news.getLink().getTitle());
        item.setDescription(news.getLink().getDescription());
        item.setPicUrl(news.getPicUrl());
        item.setUrl(news.getLink().getUrl());
        newsBuilder.addArticle(item);
      }
      outMessage =
          newsBuilder.fromUser(message.getFromUserName()).toUser(message.getToUserName()).build();
    } else {
      throw new WeixinException("不支持的消息类型");
    }
    if (StringUtil.isBlank(encryptType) || "raw".equals(encryptType)) {
      return XStreamTransformer.toXml((Class) this.getClass(), this);
    } else if ("aes".equals(encryptType)) {
      return outMessage.toEncryptedXml(wxCpConfigStorage);
    } else {
      throw new WeixinException("不可识别的加密类型");
    }
  }

  @Override
  public void sendImageMessage(Image content, String... toUsers) throws WeixinException {
    try {
      if (toUsers.length == 0) {
        this.sendImageMessage(content, -1);
        return;
      }
      // 上传图片文件
      Media media = content.getMedia();
      media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
      if (toUsers.length == 1) {
        wxCpService.messageSend(
            WxCpMessage.IMAGE().toUser(toUsers[0]).mediaId(media.getId()).build());
      } else {
        for (String toUser : toUsers) {
          this.sendImageMessage(content, toUser);
        }
      }
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public void sendImageMessage(Image content, long toGroup) throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }

  @Override
  public void sendVoiceMessage(Voice content, String... toUsers) throws WeixinException {
    try {
      if (toUsers.length == 0) {
        this.sendVoiceMessage(content, -1);
        return;
      }
      // 上传语言文件
      Media media = content.getMedia();
      media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
      if (toUsers.length == 1) {
        wxCpService.messageSend(
            WxCpMessage.VOICE().toUser(toUsers[0]).mediaId(media.getId()).build());
      } else {
        for (String toUser : toUsers) {
          this.sendVoiceMessage(content, toUser);
        }
      }
    } catch (WxErrorException | WeixinException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public void sendVoiceMessage(Voice content, long toGroup) throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }

  @Override
  public void sendVideoMessage(Video content, String... toUsers) throws WeixinException {
    try {
      if (toUsers.length == 0) {
        this.sendVideoMessage(content, -1);
        return;
      }
      // 上传视频
      Media media = content.getMedia();
      media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
      // 发送消息
      if (toUsers.length == 1) {
        // 上传缩略图
        Media thumb = content.getThumb();
        thumb.setId(this.mediaUpload(thumb.getType(), thumb.getFileItem()));
        VideoBuilder videoBuilder =
            WxCpMessage.VIDEO()
                .toUser(toUsers[0])
                .mediaId(media.getId())
                .thumbMediaId(thumb.getId());
        if (StringUtil.isNotBlank(content.getTitle())) {
          videoBuilder.title(content.getTitle());
        }
        if (StringUtil.isNotBlank(content.getDescription())) {
          videoBuilder.description(content.getDescription());
        }
        wxCpService.messageSend(videoBuilder.build());
      } else {
        for (String toUser : toUsers) {
          this.sendVideoMessage(content, toUser);
        }
      }
    } catch (WxErrorException | WeixinException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public void sendVideoMessage(Video content, long toGroup) throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }

  @Override
  public void sendMusicMessage(Music content, String toUser) throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }

  @Override
  public void sendNewsMessage(List<News> content, String toUser) throws WeixinException {
    try {
      me.chanjar.weixin.cp.bean.messagebuilder.NewsBuilder newsBuilder =
          WxCpMessage.NEWS().toUser(toUser);
      for (News news : content) {
        WxCpMessage.WxArticle article = new WxCpMessage.WxArticle();
        article.setPicUrl(news.getPicUrl());
        article.setTitle(news.getLink().getTitle());
        article.setDescription(news.getLink().getDescription());
        article.setUrl(news.getLink().getUrl());
        newsBuilder.addArticle(article);
      }
      wxCpService.messageSend(newsBuilder.build());
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  public void sendNewsMessage(List<Article> articles, String... toUsers) throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }

  @Override
  public void sendNewsMessage(List<Article> articles, long toGroup) throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }

  @Override
  public void sendTemplateMessage(Template content, String toUser) throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }

  @Override
  public void sendTextMessage(String content, String... toUsers) throws WeixinException {
    try {
      if (toUsers.length == 0) {
        sendTextMessage(content, -1);
      } else if (toUsers.length == 1) {
        wxCpService.messageSend(WxCpMessage.TEXT().toUser(toUsers[0]).content(content).build());
      } else {
        for (String toUser : toUsers) {
          this.sendTextMessage(content, toUser);
        }
      }
    } catch (WxErrorException | WeixinException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public void sendTextMessage(String content, long toGroup) throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }

  @Override
  public List<User> getUsers() throws WeixinException {
    throw new WeixinException("未实现方法");
  }

  @Override
  public OpenIdList getOpenIds() {
    return null;
  }

  @Override
  public OpenIdList getOpenIds(String nextOpenId) {
    return null;
  }

  @Override
  public User getUser(String userId) throws WeixinException {
    try {
      return toUser(wxCpService.userGet(userId));
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  private User toUser(WxCpUser wxCpUser) {
    if (wxCpUser == null) {
      return null;
    }
    return null;
  }

  @Override
  public String mediaUpload(Media.Type mediaType, Object fileItem) throws WeixinException {
    throw new WeixinException("未实现方法");
  }

  public FileObject mediaDownload(String mediaId) throws WeixinException {
    throw new WeixinException("未实现方法");
  }

  @Override
  public void refreshMenu(Menu... menus) throws WeixinException {
    throw new WeixinException("未实现方法");
  }

  public Jsapi getJsapi() {
    return this.jsapi;
  }

  @Override
  public List<Menu> getMenus() throws WeixinException {
    try {
      WxMenu wxMenu = wxCpService.menuGet();
      List<Menu> menus = new ArrayList<>(wxMenu.getButtons().size());
      for (WxMenuButton button : wxMenu.getButtons()) {
        Menu.MenuType type =
            StringUtil.isBlank(button.getType())
                ? Menu.MenuType.UNKNOWN
                : Menu.MenuType.valueOf(button.getType().toUpperCase());
        if (button.getSubButtons().isEmpty()) {
          menus.add(
              new Menu(
                  type,
                  button.getName(),
                  StringUtil.defaultValue(button.getKey(), button.getUrl())));
        } else {
          List<Menu> subMenus = new ArrayList<>();
          for (WxMenuButton wxMenuButton : button.getSubButtons()) {
            subMenus.add(
                new Menu(
                    Menu.MenuType.valueOf(wxMenuButton.getType().toUpperCase()),
                    wxMenuButton.getName(),
                    StringUtil.defaultValue(wxMenuButton.getKey(), wxMenuButton.getUrl())));
          }
          menus.add(
              new Menu(
                  type,
                  button.getName(),
                  StringUtil.defaultValue(button.getKey(), button.getUrl()),
                  subMenus.toArray(new Menu[0])));
        }
      }
      return menus;
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public void clearMenu() throws WeixinException {
    try {
      wxCpService.menuDelete();
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public Openapi getOpenapi() throws WeixinException {
    throw new WeixinException("企业号不支持该接口");
  }
}
