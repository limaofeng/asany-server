package cn.asany.weixin.framework.core;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.WxMpMassTagMessage;
import me.chanjar.weixin.mp.bean.WxMpMassVideo;
import me.chanjar.weixin.mp.bean.message.*;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.builder.outxml.MusicBuilder;
import me.chanjar.weixin.mp.builder.outxml.NewsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.*;
import cn.asany.weixin.framework.message.content.*;
import cn.asany.weixin.framework.message.user.OpenIdList;
import cn.asany.weixin.framework.message.user.User;
import cn.asany.weixin.framework.util.WeixinUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WeixinMpService implements WeixinService {

    private static final Log LOG = LogFactory.getLog(WeixinMpService.class);
    private final WxMpService wxMpService;
    private final WxMpConfigStorage wxMpConfigStorage;
    private final WxMpUserService userService;
    private final Jsapi jsapi;
    private final Openapi openapi;

    private UploadService uploadService;

    public WeixinMpService(WxMpService wxMpService, WxMpConfigStorage wxMpConfigStorage) {
        this.wxMpService = wxMpService;
        this.wxMpConfigStorage = wxMpConfigStorage;
        this.userService = wxMpService.getUserService();
        this.jsapi = new MpJsapi(this.wxMpService);
        this.openapi = new MpOpenapi(this.wxMpService);
    }

    @Override
    public WeixinMessage<?> parseInMessage(HttpServletRequest request) throws WeixinException {
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String encryptType = StringUtil.defaultValue(request.getParameter("encrypt_type"),"raw");
        String msgSignature = request.getParameter("msg_signature");
        InputStream input;
        try {
            input = request.getInputStream();
        } catch (IOException e) {
            throw new WeixinException(e.getMessage(), e);
        }


        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            throw new WeixinException("非法请求");
        }

        WxMpXmlMessage inMessage;
        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            inMessage = WxMpXmlMessage.fromXml(input);
        } else if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            inMessage = WxMpXmlMessage.fromEncryptedXml(input, wxMpConfigStorage, timestamp, nonce, msgSignature);
        } else {
            throw new WeixinException("不可识别的加密类型");
        }
        LOG.debug("inMessage=>" + JSON.serialize(inMessage));
        switch (inMessage.getMsgType()) {
            case "text":
                return MessageFactory.createTextMessage(inMessage.getMsgId(), inMessage.getFriendUserName(), new Date(inMessage.getCreateTime()), inMessage.getContent());
            case "image":
                return MessageFactory.createImageMessage(this, inMessage.getMsgId(), inMessage.getFriendUserName(), new Date(inMessage.getCreateTime()), inMessage.getMediaId(), inMessage.getUrl());
            case "voice":
                return MessageFactory.createVoiceMessage(inMessage.getMsgId(), inMessage.getFriendUserName(), new Date(inMessage.getCreateTime()), inMessage.getMediaId(), inMessage.getFormat(), inMessage.getRecognition());
            case "video":
                return MessageFactory.createVideoMessage(inMessage.getMsgId(), inMessage.getFriendUserName(), new Date(inMessage.getCreateTime()), inMessage.getMediaId(), inMessage.getThumbMediaId());
            case "location":
                return MessageFactory.createLocationMessage(inMessage.getMsgId(), inMessage.getFriendUserName(), new Date(inMessage.getCreateTime()), inMessage.getLocationX(), inMessage.getLocationY(), inMessage.getScale(), inMessage.getLabel());
            case "link":
                return MessageFactory.createLinkMessage(inMessage.getMsgId(), inMessage.getFriendUserName(), new Date(inMessage.getCreateTime()), inMessage.getTitle(), inMessage.getDescription(), inMessage.getUrl());
            case "event":
                return MessageFactory.createEventMessage(inMessage.getMsgId(), inMessage.getFriendUserName(), new Date(inMessage.getCreateTime()), inMessage.getEvent(), inMessage.getEventKey(), inMessage.getTicket(), inMessage.getLatitude(), inMessage.getLongitude(), inMessage.getPrecision());
            default:
                LOG.debug(inMessage);
                throw new WeixinException("无法处理的消息类型" + inMessage.getMsgType());
        }
    }

    @Override
    public void sendImageMessage(Image content, long tagId) throws WeixinException {
        try {
            //上传图片文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            WxMpMassTagMessage groupMessage = new WxMpMassTagMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_IMAGE);
            if (tagId != -1) {
                groupMessage.setTagId(tagId);
            }
            groupMessage.setMediaId(media.getId());
            this.wxMpService.massGroupMessageSend(groupMessage);
        } catch (WxErrorException | WeixinException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    @Override
    public String mediaUpload(Media.Type mediaType, Object fileItem) throws WeixinException {
        throw new WeixinException("未实现方法");
    }

    @Override
    public void sendImageMessage(Image content, String... toUsers) throws WeixinException {
        try {
            if (toUsers.length == 0) {
                this.sendImageMessage(content, -1);
                return;
            }
            //上传图片文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), null));
            if (toUsers.length == 1) {
                WxMpMassOpenIdsMessage message = new WxMpMassOpenIdsMessage();
                message.setMediaId(media.getId());
                message.setToUsers(Arrays.asList(toUsers));
                this.wxMpService.massOpenIdsMessageSend(message);
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_IMAGE);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setMediaId(media.getId());
                this.wxMpService.massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    @Override
    public String parseInMessage(String encryptType, WeixinMessage<?> message) throws WeixinException {
        WxMpXmlOutMessage outMessage;
        if (message instanceof TextMessage) {
            outMessage = WxMpXmlOutMessage.TEXT()
                    .content(((TextMessage) message).getContent())
                    .fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof ImageMessage) {
            Media media = ((ImageMessage) message).getContent().getMedia();
            media.setId(this.mediaUpload(media.getType(), null));
            outMessage = WxMpXmlOutMessage.IMAGE().mediaId(media.getId()).fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof VoiceMessage) {
            Media media = ((VoiceMessage) message).getContent().getMedia();
            media.setId(this.mediaUpload(media.getType(), null));
            outMessage = WxMpXmlOutMessage.VOICE().mediaId(media.getId()).fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof VideoMessage) {
            Media media = ((VideoMessage) message).getContent().getMedia();
            media.setId(this.mediaUpload(media.getType(), null));
            outMessage = WxMpXmlOutMessage.VIDEO().mediaId(media.getId()).fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof MusicMessage) {
            Music music = ((MusicMessage) message).getContent();
            Media thumb = music.getThumb();
            thumb.setId(this.mediaUpload(thumb.getType(), null));
            outMessage = WxMpXmlOutMessage.MUSIC().musicUrl(music.getUrl()).hqMusicUrl(music.getHqUrl()).title(music.getTitle()).description(music.getDescription()).thumbMediaId(thumb.getId()).fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof NewsMessage) {
            List<News> newses = ((NewsMessage) message).getContent();
            NewsBuilder newsBuilder = WxMpXmlOutMessage.NEWS();
            for (News news : newses) {
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setTitle(news.getLink().getTitle());
                item.setDescription(news.getLink().getDescription());
                item.setPicUrl(news.getPicUrl());
                item.setUrl(news.getLink().getUrl());
                newsBuilder.addArticle(item);
            }
            outMessage = newsBuilder.fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else {
            throw new WeixinException("不支持的消息类型");
        }
        if (StringUtils.isBlank(encryptType) || "raw".equals(encryptType)) {
            return outMessage.toXml();
        } else if ("aes".equals(encryptType)) {
            return outMessage.toEncryptedXml(wxMpConfigStorage);
        } else {
            throw new WeixinException("不可识别的加密类型");
        }
    }

    @Override
    public void sendVoiceMessage(Voice content, String... toUsers) throws WeixinException {
        try {
            if (toUsers.length == 0) {
                this.sendVoiceMessage(content, -1);
                return;
            }
            //上传语言文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), null));//media.getFileItem()
            if (toUsers.length == 1) {
                //wxMpService.customMessageSend(WxMpCustomMessage.VOICE().toUser(toUsers[0]).mediaId(media.getId()).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_VOICE);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setMediaId(media.getId());
                wxMpService.massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException | WeixinException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVoiceMessage(Voice content, long toGroup) throws WeixinException {
//        try {
        //上传语言文件
        Media media = content.getMedia();
        media.setId(this.mediaUpload(media.getType(), null));//media.getFileItem()
        //WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
        //groupMessage.setMsgtype(WxConsts.MASS_MSG_VOICE);
        //if (toGroup != -1) {
        //   groupMessage.setGroupId(toGroup);
        //}
        //groupMessage.setMediaId(media.getId());
        //wxMpService.massGroupMessageSend(groupMessage);
//        } catch (WxErrorException e) {
//            throw new WeiXinException(e.getMessage(), e);
//        }
    }

    @Override
    public void sendVideoMessage(Video content, String... toUsers) throws WeixinException {
        try {
            if (toUsers.length == 0) {
                this.sendVideoMessage(content, -1);
                return;
            }
            //上传视频
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), null));//media.getFileItem()
            //发送消息
            WxMpMassVideo massVideo = new WxMpMassVideo();
            massVideo.setMediaId(media.getId());
            massVideo.setTitle(content.getTitle());
            massVideo.setDescription(content.getDescription());
            WxMpMassUploadResult result = wxMpService.massVideoUpload(massVideo);
            WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
            openIdsMessage.setMsgType(WxConsts.MASS_MSG_VIDEO);
            for (String toUser : toUsers) {
                openIdsMessage.addUser(toUser);
            }
            openIdsMessage.setMediaId(result.getMediaId());
            wxMpService.massOpenIdsMessageSend(openIdsMessage);
        } catch (WxErrorException | WeixinException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVideoMessage(Video content, long toGroup) throws WeixinException {
        try {
            //上传视频
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), null));//media.getFileItem()
            //发送消息
            WxMpMassVideo massVideo = new WxMpMassVideo();
            massVideo.setMediaId(media.getId());
            massVideo.setTitle(content.getTitle());
            massVideo.setDescription(content.getDescription());
            WxMpMassUploadResult result = wxMpService.massVideoUpload(massVideo);
            WxMpMassTagMessage message = new WxMpMassTagMessage();
            message.setMsgtype(WxConsts.MASS_MSG_VIDEO);
            if (toGroup != -1) {
                message.setTagId(toGroup);
            }
            message.setMediaId(result.getMediaId());
            wxMpService.massGroupMessageSend(message);
        } catch (WxErrorException | WeixinException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendMusicMessage(Music content, String toUser) throws WeixinException {
        try {
            //上传缩略图
            Media thumb = content.getThumb();
            thumb.setId(this.mediaUpload(thumb.getType(), null));//thumb.getFileItem()
            //发送消息
            MusicBuilder musicBuilder = WxMpXmlOutVideoMessage.MUSIC().toUser(toUser).musicUrl(content.getUrl()).hqMusicUrl(content.getHqUrl()).thumbMediaId(thumb.getId());
            if (StringUtil.isNotBlank(content.getTitle())) {
                musicBuilder.title(content.getTitle());
            }
            if (StringUtil.isNotBlank(content.getDescription())) {
                musicBuilder.description(content.getDescription());
            }
        } catch (WeixinException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendNewsMessage(List<News> content, String toUser) throws WeixinException {
         /*
        try {

            me.chanjar.weixin.mp.bean.custombuilder.NewsBuilder newsBuilder = WxMpCustomMessage.NEWS().toUser(toUser);
            for (News news : content) {
                WxMpCustomMessage.WxArticle article = new WxMpCustomMessage.WxArticle();
                article.setPicUrl(news.getPicUrl());
                article.setTitle(news.getLink().getTitle());
                article.setDescription(news.getLink().getDescription());
                article.setUrl(news.getLink().getUrl());
                newsBuilder.addArticle(article);
            }
            wxMpService.customMessageSend(newsBuilder.build());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
        */
    }

    @Override
    public void sendNewsMessage(List<Article> articles, String... toUsers) throws WeixinException {
        if (toUsers.length == 0) {
            this.sendNewsMessage(articles, -1);
            return;
        }
        try {
            WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
            openIdsMessage.setMsgType(WxConsts.MASS_MSG_NEWS);
            for (String toUser : toUsers) {
                openIdsMessage.addUser(toUser);
            }
            WxMpMassNews massNews = new WxMpMassNews();
            for (Article article : articles) {
                WxMpMassNews.WxMpMassNewsArticle newsArticle = new WxMpMassNews.WxMpMassNewsArticle();
                article.getThumb().setId(this.mediaUpload(article.getThumb().getType(), article.getThumb().getFileItem()));
                newsArticle.setThumbMediaId(article.getThumb().getId());
                newsArticle.setTitle(article.getTitle());
                newsArticle.setContent(article.getContent());
                newsArticle.setShowCoverPic(article.isShowCoverPic());
                if (StringUtil.isNotBlank(article.getAuthor())) {
                    newsArticle.setAuthor(newsArticle.getAuthor());
                }
                if (StringUtil.isNotBlank(article.getContentSourceUrl())) {
                    newsArticle.setContentSourceUrl(newsArticle.getContentSourceUrl());
                }
                if (StringUtil.isNotBlank(article.getDigest())) {
                    newsArticle.setDigest(article.getDigest());
                }
                massNews.addArticle(newsArticle);
            }
            WxMpMassUploadResult result = wxMpService.massNewsUpload(massNews);
            openIdsMessage.setMediaId(result.getMediaId());
            wxMpService.massOpenIdsMessageSend(openIdsMessage);
        } catch (WxErrorException | WeixinException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendNewsMessage(List<Article> articles, long toGroup) throws WeixinException {
        /*
        try {
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_NEWS);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            WxMpMassNews massNews = new WxMpMassNews();
            for (Article article : articles) {
                WxMpMassNews.WxMpMassNewsArticle newsArticle = new WxMpMassNews.WxMpMassNewsArticle();
                article.getThumb().setId(this.mediaUpload(article.getThumb().getType(), article.getThumb().getFileItem()));
                newsArticle.setThumbMediaId(article.getThumb().getId());
                newsArticle.setTitle(article.getTitle());
                newsArticle.setContent(article.getContent());
                newsArticle.setShowCoverPic(article.isShowCoverPic());
                if (StringUtil.isNotBlank(article.getAuthor())) {
                    newsArticle.setAuthor(newsArticle.getAuthor());
                }
                if (StringUtil.isNotBlank(article.getContentSourceUrl())) {
                    newsArticle.setContentSourceUrl(newsArticle.getContentSourceUrl());
                }
                if (StringUtil.isNotBlank(article.getDigest())) {
                    newsArticle.setDigest(article.getDigest());
                }
                massNews.addArticle(newsArticle);
            }
            WxMpMassUploadResult result = wxMpService.massNewsUpload(massNews);
            groupMessage.setMediaId(result.getMediaId());
            wxMpService.massGroupMessageSend(groupMessage);
        } catch (WxErrorException | WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
        */
    }

    public void sendTemplateMessage(Template content, String toUser) throws WeixinException {
        /*
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        List<WxMpTemplateData> wxMpTemplateDatas = new ArrayList<WxMpTemplateData>();
        for (Map.Entry<String, Template.Data> entry : content.getDatas().entrySet()) {
            WxMpTemplateData wxMpTemplateData = new WxMpTemplateData();
            wxMpTemplateData.setName(entry.getKey());
            wxMpTemplateData.setValue(entry.getValue().getValue());
            wxMpTemplateData.setColor(entry.getValue().getColor());
            wxMpTemplateDatas.add(wxMpTemplateData);
        }
        try {
            wxMpTemplateMessage.setTemplateId(content.getTemplateId());
            wxMpTemplateMessage.setDatas(wxMpTemplateDatas);
            wxMpTemplateMessage.setTopColor(content.getTopColor());
            wxMpTemplateMessage.setUrl(content.getUrl());
            wxMpTemplateMessage.setToUser(toUser);
            wxMpService.templateSend(wxMpTemplateMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
        */
    }

    @Override
    public void sendTextMessage(String content, String... toUsers) throws WeixinException {
        /*
        try {
            if (toUsers.length == 0) {
                sendTextMessage(content, -1);
            } else if (toUsers.length == 1) {
                wxMpService.customMessageSend(WxMpCustomMessage.TEXT().toUser(toUsers[0]).content(content).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_TEXT);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setContent(content);
                wxMpService.massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException | WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
        */
    }

    @Override
    public void sendTextMessage(String content, long tag) throws WeixinException {
        try {
            WxMpMassTagMessage message = new WxMpMassTagMessage();
            message.setMsgtype(WxConsts.MASS_MSG_TEXT);
            if (tag != -1) {
                message.setTagId(tag);
            }
            message.setContent(content);
            wxMpService.massGroupMessageSend(message);
        } catch (WxErrorException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }


    @Override
    public List<User> getUsers() throws WeixinException {
        try {
            List<User> users = new ArrayList<>();
            WxMpUserList userList = userService.userList(null);
            if (userList.getTotal() > userList.getCount()) {
                throw new WeixinException("微信关注粉丝超出程序设计值，请联系开发人员。重新设计");
            }
            for (String openId : userList.getOpenIds()) {
                users.add(getUser(openId));
            }
            return users;
        } catch (WxErrorException | WeixinException e) {
            throw new WeixinException(e.getMessage(), e);
        }
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
            return WeixinUtil.toUser(wxMpService.getUserService().userInfo(userId, null));
        } catch (WxErrorException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    public FileObject mediaDownload(String mediaId) throws WeixinException {
        WxMpMaterialService materialService = wxMpService.getMaterialService();
        try {
            File file = materialService.mediaDownload(mediaId);
            if (file == null) {
                return null;
            }
            return null;//fileManager.retrieveFileItem(file);
        } catch (WxErrorException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }


    @Override
    public void refreshMenu(Menu... menus) throws WeixinException {
        WxMpMenuService menuService = this.wxMpService.getMenuService();
        WxMenu wxMenu = new WxMenu();
        for (Menu menu : menus) {
            WxMenuButton wxMenuButton = new WxMenuButton();
            wxMenuButton.setName(menu.getName());
            wxMenuButton.setType(menu.getType().getValue());
            wxMenuButton.setUrl(menu.getUrl());
            wxMenuButton.setKey(menu.getKey());

            for (Menu subMenu : menu.getChildren()) {
                WxMenuButton subWxMenuButton = new WxMenuButton();
                subWxMenuButton.setName(subMenu.getName());
                subWxMenuButton.setType(subMenu.getType().getValue());
                subWxMenuButton.setUrl(subMenu.getUrl());
                subWxMenuButton.setKey(subMenu.getKey());
                wxMenuButton.getSubButtons().add(subWxMenuButton);
            }

            wxMenu.getButtons().add(wxMenuButton);
        }
        try {
            menuService.menuCreate(wxMenu);
        } catch (WxErrorException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

    public Jsapi getJsapi() throws WeixinException {
        return this.jsapi;
    }

    public Openapi getOpenapi() throws WeixinException {
        return this.openapi;
    }

    @Override
    public List<Menu> getMenus() throws WeixinException {
        WxMpMenuService menuService = this.wxMpService.getMenuService();
        try {
            WxMenu wxMenu = menuService.menuGet();
            List<Menu> menus = new ArrayList<>(wxMenu.getButtons().size());
            for (WxMenuButton button : wxMenu.getButtons()) {
                Menu.MenuType type = StringUtil.isBlank(button.getType()) ? Menu.MenuType.UNKNOWN : Menu.MenuType.valueOf(button.getType().toUpperCase());
                if (button.getSubButtons().isEmpty()) {
                    menus.add(new Menu(type, button.getName(), StringUtil.defaultValue(button.getKey(), button.getUrl())));
                } else {
                    List<Menu> subMenus = new ArrayList<>();
                    for (WxMenuButton wxMenuButton : button.getSubButtons()) {
                        subMenus.add(new Menu(Menu.MenuType.valueOf(wxMenuButton.getType().toUpperCase()), wxMenuButton.getName(), StringUtil.defaultValue(wxMenuButton.getKey(), wxMenuButton.getUrl())));
                    }
                    menus.add(new Menu(type, button.getName(), StringUtil.defaultValue(button.getKey(), button.getUrl()), subMenus.toArray(new Menu[0])));
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
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new WeixinException(e.getMessage(), e);
        }
    }

}
