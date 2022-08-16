package cn.asany.base.sms;

/** 发送失败短信 */
public class SendFailedException extends Exception {

  public SendFailedException(String message) {
    super(message);
  }
}
