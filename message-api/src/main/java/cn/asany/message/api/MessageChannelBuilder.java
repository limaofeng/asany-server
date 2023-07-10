package cn.asany.message.api;

/**
 * 消息发送者构建器
 *
 * @author limaofeng
 * @param <T> 消息发送者
 * @param <C> 消息发送者配置
 */
public interface MessageChannelBuilder<T extends MessageChannel, C extends IChannelConfig> {

  /**
   * 支持
   *
   * @param clazz 类型
   * @return Boolean
   */
  boolean supports(Class<C> clazz);

  /**
   * 构建方法
   *
   * @param clazz 类型
   * @return T
   */
  T build(C clazz);
}
