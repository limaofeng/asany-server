package cn.asany.message.api;

import java.util.Map;

/**
 * 消息服务
 *
 * @author limaofeng
 */
public interface MessageService {

  /**
   * 发送消息
   *
   * @param type 消息类型
   * @param content 消息内容
   * @param receivers 接收人
   * @return 消息ID
   */
  String send(String type, String content, String... receivers);

  /**
   * 发送消息
   *
   * @param type 消息类型
   * @param title 消息标题
   * @param content 消息内容
   * @param receivers 接收人
   * @return 消息ID
   */
  String send(String type, String title, String content, String... receivers);

  /**
   * 发送消息
   *
   * @param type 消息类型
   * @param variables 消息变量
   * @param receivers 接收人
   * @return 消息ID
   */
  String send(String type, Map<String, Object> variables, String... receivers);
}
