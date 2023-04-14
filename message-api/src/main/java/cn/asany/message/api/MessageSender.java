package cn.asany.message.api;

/**
 * 消息发送者
 *
 * @author limaofeng
 */
public interface MessageSender {

  /**
   * 发送消息
   *
   * @param simpleMessage 消息
   * @throws MessageException 消息异常
   */
  void send(SimpleMessage simpleMessage) throws MessageException;
}
