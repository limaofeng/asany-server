package cn.asany.weixin.service;

import cn.asany.weixin.bean.Fans;
import cn.asany.weixin.bean.UserKey;
import cn.asany.weixin.dao.FansDao;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.factory.WeixinSessionUtils;
import cn.asany.weixin.framework.message.user.User;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FansService {

  private final FansDao fansDao;

  @Autowired
  public FansService(FansDao fansDao) {
    this.fansDao = fansDao;
  }

  public Fans get(String appId, String openId) {
    return fansDao.getById(new UserKey(appId, openId));
  }

  public Fans get(UserKey key) {
    return fansDao.getById(key);
  }

  public Fans save(Fans ui) {
    return fansDao.save(ui);
  }

  public void delete(UserKey... keys) {
    fansDao.deleteAllById(Arrays.asList(keys));
  }

  public void delete(UserKey key) {
    this.fansDao.deleteById(key);
  }

  public Pager<Fans> findPager(Pager<Fans> pager, List<PropertyFilter> filters) {
    return this.fansDao.findPager(pager, filters);
  }

  public Fans save(String id, User user) {
    Optional<Fans> optional = this.fansDao.findById(UserKey.newInstance(id, user.getOpenId()));
    Fans fans = optional.orElseGet(() -> Fans.builder().appId(id).build());
    return this.fansDao.save(merge(fans, user));
  }

  /** 通过openId刷新用户信息 */
  private Fans refresh(String appid, String openId) throws WeixinException {
    Fans ui = get(UserKey.newInstance(appid, openId));
    if (ui != null) {
      return ui;
    }
    User user = WeixinSessionUtils.getCurrentSession().getUser(openId);
    if (user == null) {
      return null;
    }
    ui = merge(new Fans(), user);
    ui.setAppId(appid);
    this.fansDao.save(ui);
    return ui;
  }

  /**
   * 转换user到userinfo
   *
   * @param fans 微信粉丝
   * @param user 微信平台对象
   * @return Fans
   */
  private Fans merge(Fans fans, User user) {
    fans.setOpenId(user.getOpenId());
    fans.setAvatar(user.getAvatar());
    fans.setCity(user.getCity());
    fans.setCountry(user.getCountry());
    fans.setProvince(user.getProvince());
    fans.setLanguage(user.getLanguage());
    fans.setNickname(user.getNickname());
    fans.setSex(user.getSex());
    fans.setSubscribe(user.isSubscribe());
    if (user.getSubscribeTime() != null) {
      fans.setSubscribeTime(user.getSubscribeTime().getTime());
    }
    fans.setUnionId(user.getUnionid());
    return fans;
  }

  public Fans checkCreateMember(String appid, String openId) throws WeixinException {
    return refresh(appid, openId);
  }
}
