package cn.asany.weixin.framework.util;

import cn.asany.weixin.framework.message.user.Sex;
import cn.asany.weixin.framework.message.user.User;
import java.util.Date;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

public class WeixinUtil {

  private WeixinUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static User toUser(WxMpUser wxMpUser) {
    if (wxMpUser == null) {
      return null;
    }
    User user = new User();
    user.setOpenId(wxMpUser.getOpenId());
    user.setAvatar(wxMpUser.getHeadImgUrl());
    user.setCity(wxMpUser.getCity());
    user.setCountry(wxMpUser.getCountry());
    user.setProvince(wxMpUser.getProvince());
    user.setLanguage(wxMpUser.getLanguage());
    user.setNickname(wxMpUser.getNickname());
    user.setSex(toSex(wxMpUser.getSex()));
    user.setSubscribe(wxMpUser.getSubscribe());
    if (wxMpUser.getSubscribeTime() != null) {
      user.setSubscribeTime(new Date(wxMpUser.getSubscribeTime()));
    }
    user.setUnionid(wxMpUser.getUnionId());
    return user;
  }

  private static Sex toSex(String sex) {
    if ("男".equals(sex)) {
      return Sex.male;
    }
    if ("女".equals(sex)) {
      return Sex.female;
    }
    return Sex.unknown;
  }
}
