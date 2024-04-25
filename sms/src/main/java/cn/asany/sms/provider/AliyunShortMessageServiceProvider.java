/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
