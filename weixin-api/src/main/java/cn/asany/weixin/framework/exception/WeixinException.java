package cn.asany.weixin.framework.exception;

/**
 * 微信异常
 *
 * @author limaofeng
 */
public class WeixinException extends Exception {

  public WeixinException(String message) {
    super(message);
  }

  public WeixinException(String message, Exception e) {
    super(message, e);
  }
}
