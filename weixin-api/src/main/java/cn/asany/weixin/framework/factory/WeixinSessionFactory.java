package cn.asany.weixin.framework.factory;

import cn.asany.weixin.framework.account.WeixinAppService;
import cn.asany.weixin.framework.core.WeixinCoreHelper;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;

/** 微信会话工厂 */
public interface WeixinSessionFactory {

  /**
   * 第三方工具类
   *
   * @return Signature
   */
  WeixinCoreHelper getWeixinCoreHelper();

  /**
   * 获取当前的 WeiXinSession
   *
   * @return WeiXinSession
   * @throws WeixinException
   */
  WeixinSession getCurrentSession() throws WeixinException;

  /**
   * 返回一个 WeiXinSession 对象，如果当前不存在，则创建一个新的session对象
   *
   * @return WeiXinSession
   * @throws WeixinException
   */
  WeixinSession openSession(String appid) throws WeixinException;

  /**
   * 获取微信账号存储服务
   *
   * @return AccountDetailsService
   */
  WeixinAppService getWeixinAppService();

  /**
   * 处理接收到的请求
   *
   * @param message http response
   * @return WeiXinMessage
   */
  WeixinMessage<?> execute(WeixinMessage message) throws WeixinException;
}
