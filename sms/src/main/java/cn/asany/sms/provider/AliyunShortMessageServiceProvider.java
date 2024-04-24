package cn.asany.sms.provider;

import cn.asany.base.sms.SMSProviderConfig;
import cn.asany.base.sms.SendFailedException;
import cn.asany.base.sms.ShortMessageServiceProvider;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;

/**
 * 阿里云短信发送服务
 *
 * @author limaofeng
 */
@Slf4j
public class AliyunShortMessageServiceProvider implements ShortMessageServiceProvider {

  private Client client;

  @Override
  public void configure(SMSProviderConfig config) throws Exception {
    AliyunSMSProviderConfig aliyunProviderConfig = (AliyunSMSProviderConfig) config;
    String accessKeyId = aliyunProviderConfig.getAccessKeyId();
    String accessKeySecret = aliyunProviderConfig.getAccessKeySecret();
    Config aliyunConfig =
        new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
    aliyunConfig.endpoint = "dysmsapi.aliyuncs.com";
    this.client = new com.aliyun.dysmsapi20170525.Client(aliyunConfig);
  }

  @Override
  public String send(String template, Map<String, String> params, String sign, String... phones)
      throws SendFailedException {
    SendSmsRequest sendSmsRequest =
        new SendSmsRequest()
            .setSignName(sign)
            .setTemplateCode(template)
            .setPhoneNumbers(StringUtil.join(phones, ","))
            .setTemplateParam(JSON.serialize(params));
    RuntimeOptions runtime = new RuntimeOptions();
    try {
      // 复制代码运行请自行打印 API 的返回值
      SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
      return JSON.serialize(response.getBody());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new SendFailedException(e.getMessage());
    }
  }
}
