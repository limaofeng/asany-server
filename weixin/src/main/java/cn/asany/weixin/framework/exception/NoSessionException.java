package cn.asany.weixin.framework.exception;

/** session 不存在 */
public class NoSessionException extends WeixinException {

  public NoSessionException(String message) {
    super(message);
  }
}
