package cn.asany.weixin.framework.exception;

/** 微信账号不存在 */
public class AppidNotFoundException extends WeixinException {

  public AppidNotFoundException(String message) {
    super(message);
  }
}
