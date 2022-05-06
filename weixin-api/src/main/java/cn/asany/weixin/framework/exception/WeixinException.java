package cn.asany.weixin.framework.exception;

/** 微信异常 Created by zzzhong on 2014/12/4. */
public class WeixinException extends Exception {

  public WeixinException(String message) {
    super(message);
  }

  public WeixinException(String message, Exception e) {
    super(message, e);
  }
}
