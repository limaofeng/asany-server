package cn.asany.message.api;

/**
 * 消息发送者
 *
 * @author limaofeng
 */
public interface MessageChannel {

  /**
   * 发送消息
   *
   * @param message 消息
   * @throws MessageException 消息异常
   */
  void send(Message message) throws MessageException;
}
