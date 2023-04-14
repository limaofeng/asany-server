package cn.asany.autoconfigure;

import cn.asany.message.api.MessageException;
import cn.asany.message.api.MessageSender;
import cn.asany.message.api.SMSSenderConfig;
import cn.asany.message.api.SimpleMessage;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.SneakyThrows;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * 短信发送器
 *
 * @author limaofeng
 */
public class ShortMessageSender implements MessageSender {

  private final Client client;

  @SneakyThrows({Exception.class})
  public ShortMessageSender(SMSSenderConfig config) {
    String accessKeyId = config.getAccessKeyId();
    String accessKeySecret = config.getAccessKeySecret();
    Config config1 = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
    // 访问的域名
    config1.endpoint = config.getEndpoint();
    this.client = new com.aliyun.dysmsapi20170525.Client(config1);
  }

  @Override
  public void send(SimpleMessage simpleMessage) throws MessageException {
    SendSmsRequest sendSmsRequest =
        new SendSmsRequest()
            .setSignName(simpleMessage.getSignName())
            .setTemplateCode(simpleMessage.getTemplateCode())
            .setPhoneNumbers(StringUtil.join(new String[] {"phones"}, ","))
            .setTemplateParam(JSON.serialize(simpleMessage.getTemplateParams()));
    RuntimeOptions runtime = new RuntimeOptions();
    SendSmsResponse response = null;
    try {
      response = client.sendSmsWithOptions(sendSmsRequest, runtime);
    } catch (Exception e) {
      throw new MessageException(e.getMessage());
    }
    JSON.serialize(response.getBody());
  }
}
