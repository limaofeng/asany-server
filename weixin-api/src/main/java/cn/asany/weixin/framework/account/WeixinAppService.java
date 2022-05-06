package cn.asany.weixin.framework.account;

import cn.asany.weixin.framework.exception.AppidNotFoundException;
import cn.asany.weixin.framework.session.WeixinApp;
import java.util.List;

/** 微信公众号接口 */
public interface WeixinAppService {

  /**
   * 通过 appid 获取微信号配置信息
   *
   * @param appid appid
   * @return AccountDetails
   * @throws AppidNotFoundException
   */
  WeixinApp loadAccountByAppid(String appid) throws AppidNotFoundException;

  /**
   * 获取全部的微信公众号信息
   *
   * @return List<AccountDetails>
   */
  List<WeixinApp> getAll();
}
