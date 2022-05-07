package cn.asany.weixin.framework.session;

import cn.asany.weixin.framework.exception.WeixinException;

/**
 * 微信号，账号详细质料
 *
 * @author limaofeng@msn.com
 */
public interface WeixinApp {

  /**
   * 微信申请的appid
   *
   * @return appid
   */
  String getId();

  /**
   * 公众号类型
   *
   * @return type
   */
  WeixinAppType getType();

  /**
   * 密钥
   *
   * @return String
   */
  String getSecret();

  /**
   * 令牌 name
   *
   * @return String
   */
  String getToken();

  /**
   * 安全验证码
   *
   * @return String
   */
  String getAesKey();

  /**
   * 原始ID 用户消息回复时的 formusername
   *
   * @return String
   */
  String getPrimitiveId();

  /**
   * 代理ID<br>
   * 企业号才需要配置该属性
   *
   * @return Integer
   */
  Integer getAgentId();

  /**
   * 获取 WeixinSession
   *
   * @return WeixinSession
   * @throws WeixinException 微信配置异常
   */
  WeixinSession getSession() throws WeixinException;
}
