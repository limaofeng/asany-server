package cn.asany.sms.service;

import cn.asany.sms.dao.MessageDao;
import cn.asany.sms.dao.TemplateDao;
import cn.asany.sms.domain.Captcha;
import cn.asany.sms.domain.CaptchaConfig;
import cn.asany.sms.domain.ShortMessage;
import cn.asany.sms.domain.Template;
import cn.asany.sms.domain.enums.MessageStatus;
import cn.asany.sms.utils.TemplateContentUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 消息服务 */
@Service("sms.messageService")
public class MessageService {

  private final MessageDao messageDao;
  private final TemplateDao templateDao;

  @Autowired
  public MessageService(MessageDao messageDao, TemplateDao templateDao) {
    this.messageDao = messageDao;
    this.templateDao = templateDao;
  }

  @Transactional
  public Page<ShortMessage> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return this.messageDao.findPage(pageable, filters);
  }

  @Transactional
  public ShortMessage get(Long id) {
    return this.messageDao.getReferenceById(id);
  }

  @Transactional
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

  @Transactional
  public ShortMessage save(
      String sign, String template, Map<String, String> params, Long delay, String... phones) {
    ShortMessage shortMessage = new ShortMessage();
    shortMessage.setSign(sign);

    Optional<Template> templateOptional =
        this.templateDao.findOne(PropertyFilter.builder().equal("code", template).build());

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

  @Transactional
  public void update(ShortMessage message) {
    this.messageDao.update(message);
  }

  @Transactional
  public List<ShortMessage> findList(String phone, Long template, String time)
      throws ParseException {
    return this.messageDao.findAll(
        PropertyFilter.builder()
            .equal("phones", phone)
            .equal("template.id", template)
            .greaterThan("createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time))
            .build(),
        Sort.by("createdAt").descending());
  }
}
