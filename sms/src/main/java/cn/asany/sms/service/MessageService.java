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
package cn.asany.sms.service;

import cn.asany.base.sms.*;
import cn.asany.sms.dao.MessageDao;
import cn.asany.sms.dao.ProviderDao;
import cn.asany.sms.dao.TemplateDao;
import cn.asany.sms.domain.*;
import cn.asany.sms.provider.AliyunSMSProviderConfig;
import cn.asany.sms.utils.TemplateContentUtil;
import java.util.*;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.DateUtil;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息服务
 *
 * @author limaofeng
 */
@Service("sms.messageService")
public class MessageService implements ShortMessageSendService {

  private final MessageDao messageDao;
  private final TemplateDao templateDao;
  private final ProviderDao providerDao;

  @Autowired
  public MessageService(MessageDao messageDao, TemplateDao templateDao, ProviderDao providerDao) {
    this.messageDao = messageDao;
    this.templateDao = templateDao;
    this.providerDao = providerDao;
  }

  @Transactional
  public Page<ShortMessage> findPage(Pageable pageable, PropertyFilter filter) {
    return this.messageDao.findPage(pageable, filter);
  }

  @Transactional(readOnly = true)
  public ShortMessage get(Long id) {
    return this.messageDao.getReferenceById(id);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public void save(Captcha captcha) {
    CaptchaConfig config = captcha.getConfig();
    Map<String, String> params = new HashMap<>();
    params.put("code", captcha.getValue());
    ShortMessage shortMessage = new ShortMessage();
    shortMessage.setConfig(config);
    shortMessage.setSign(config.getTemplate().getSign());
    shortMessage.setTemplate(config.getTemplate());
    shortMessage.setContent(JSON.serialize(params));
    List<String> phones = new ArrayList<>();
    phones.add(captcha.getPhone());
    shortMessage.setPhones(phones);
    shortMessage.setDelay(0L);
    shortMessage.setStatus(MessageStatus.unsent);
    shortMessage.setNotes("验证码短信");
    messageDao.save(shortMessage);
  }

  @Override
  @Transactional(rollbackFor = RuntimeException.class)
  public ShortMessageInfo send(
      String template, Map<String, String> params, String sign, long delay, String... phones) {
    return send(DEFAULT_PROVIDER, template, params, sign, delay, phones);
  }

  @Override
  @Transactional(rollbackFor = RuntimeException.class)
  public ShortMessageInfo send(
      String provider, String template, Map<String, String> params, String sign, String... phones) {
    return send(provider, template, params, sign, 0L, phones);
  }

  @Override
  @Transactional(rollbackFor = RuntimeException.class)
  public ShortMessageInfo send(
      String provider,
      String template,
      Map<String, String> params,
      String sign,
      long delay,
      String... phones) {
    ShortMessage shortMessage = new ShortMessage();
    shortMessage.setSign(sign);

    Optional<Template> templateOptional =
        this.templateDao.findOne(PropertyFilter.newFilter().equal("code", template));

    Template temp = templateOptional.orElseThrow(() -> new ValidationException("短信模版不存在"));
    shortMessage.setTemplate(temp);

    String content = TemplateContentUtil.replaceVariables(params, temp.getContent());

    shortMessage.setContent(JSON.serialize(params));
    shortMessage.setNotes(content);
    shortMessage.setDelay(ObjectUtil.defaultValue(delay, 0L));
    shortMessage.setPhones(Arrays.asList(phones));
    shortMessage.setStatus(MessageStatus.unsent);
    return messageDao.save(shortMessage);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public void update(ShortMessage message) {
    this.messageDao.update(message);
  }

  @Transactional
  public List<ShortMessage> findList(String phone, Long template, String time) {
    return this.messageDao.findAll(
        PropertyFilter.newFilter()
            .equal("phones", phone)
            .equal("template.id", template)
            .greaterThan("createdAt", DateUtil.parse(time, "yyyy-MM-dd HH:mm:ss")),
        Sort.by("createdAt").descending());
  }

  public SMSProviderConfig getProviderConfig(String provider) {
    String[] keys = provider.split("\\.");
    Optional<Provider> optional =
        this.providerDao.findOne(
            PropertyFilter.newFilter().equal("id", keys[1]).equal("type", SMSProvider.of(keys[0])));
    return optional
        .map(
            p -> {
              if (p.getType() == SMSProvider.ALIYUN) {
                AliyunSMSProviderConfig config =
                    JSON.deserialize(p.getConfig(), AliyunSMSProviderConfig.class);
                config.setKey(p.getType().getValue() + "." + p.getId());
                return config;
              }
              return null;
            })
        .orElseThrow(() -> new ValidationException("短信服务商不存在"));
  }
}
