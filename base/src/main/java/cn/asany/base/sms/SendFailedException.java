package cn.asany.base.sms;

/**
 * 发送失败短信
 *
 * @author limaofeng
 */
public class SendFailedException extends Exception {

  public SendFailedException(String message) {
    super(message);
  }
}
