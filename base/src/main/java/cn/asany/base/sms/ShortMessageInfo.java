package cn.asany.base.sms;

import java.util.List;

/**
 * 短信信息
 *
 * @author limaofeng
 */
public interface ShortMessageInfo {
  /**
   * 获取短信ID
   *
   * @return 短信ID
   */
  Long getId();

  /**
   * 获取签名
   *
   * @return 签名
   */
  String getSign();
  /**
   * 获取模板ID
   *
   * @return 模板ID
   */
  String getTemplateId();
  /**
   * 获取模板内容
   *
   * @return 模板内容
   */
  String getContent();

  String getNotes();

  /**
   * 获取短信状态
   *
   * @return 短信状态
   */
  MessageStatus getStatus();

  /**
   * 接收手机号
   *
   * @return 手机号
   */
  List<String> getPhones();

  /**
   * 延迟发送
   *
   * @return 延迟发送
   */
  Long getDelay();

  /**
   * 服务提供商
   *
   * @return 服务提供商
   */
  String getProvider();
  /**
   * 获取配置ID
   *
   * @return 配置ID
   */
  String getConfigId();
}
