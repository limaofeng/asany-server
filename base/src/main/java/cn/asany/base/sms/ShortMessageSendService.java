package cn.asany.base.sms;

import java.util.Map;

/**
 * 短信发送接口
 *
 * @author 李茂峰
 * @version 1.0
 */
@FunctionalInterface
public interface ShortMessageSendService {

  String send(String template, Map<String, String> params, String sign, String... phones)
      throws SendFailedException;
}
