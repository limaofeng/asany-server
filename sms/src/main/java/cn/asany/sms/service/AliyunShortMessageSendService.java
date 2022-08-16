package cn.asany.sms.service;

import cn.asany.base.sms.SendFailedException;
import cn.asany.base.sms.ShortMessageSendService;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

@Slf4j
public class AliyunShortMessageSendService implements ShortMessageSendService {

  private final Client client;

  public AliyunShortMessageSendService(Client client) {
    this.client = client;
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
