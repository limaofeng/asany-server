package cn.asany.message.api;

/**
 * 消息发送者解析器
 *
 * @author limaofeng
 */
public interface MessageSenderResolver {

  /**
   * 解析消息发送者
   *
   * @param id 消息发送者定义ID
   * @return 消息发送者
   * @throws MessageException 消息异常
   */
  MessageSender resolve(String id) throws MessageException;

  /**
   * 解析消息发送者
   *
   * @param id 消息发送者定义ID
   * @param config 消息发送者配置
   * @return 消息发送者
   * @throws MessageException 消息异常
   */
  MessageSender resolve(String id, ISenderConfig config) throws MessageException;
}
