package cn.asany.sms.service;

import cn.asany.sms.dao.CaptchaDao;
import cn.asany.sms.domain.Captcha;
import cn.asany.sms.domain.CaptchaConfig;
import cn.asany.sms.domain.ShortMessage;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 短信服务类
 *
 * <p>
 *
 * <p>短信验证码功能要求:<br>
 * 1.可以配置短信码的长度。<br>
 * 2.可以配置短信模板。<br>
 * 3.可以配置短信过期时间。（短信重复发送时，将重发上次生成的验证码）<br>
 * 4.可以配置短信验证的过期时间。<br>
 * 5.线程定时清理无效的验证码。<br>
 * 6.验证后成功清除原验证码。<br>
 * 7.可以配置验证失败次数。（次数耗尽清除验证码）<br>
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-4 上午10:27:34
 */
@Slf4j
@Service
@Transactional
public class ValidationCaptchaService {

  private static final Pattern VALIDATOR_MOBILE =
      RegexpUtil.getPattern("^(13|14|15|17|18)[0-9]{9}$");

  private final CaptchaDao captchaDao;
  private final MessageService messageService;
  private final CaptchaService captchaConfigService;

  @Autowired
  public ValidationCaptchaService(
      CaptchaDao captchaDao, MessageService messageService, CaptchaService captchaConfigService) {
    this.captchaDao = captchaDao;
    this.messageService = messageService;
    this.captchaConfigService = captchaConfigService;
  }

  /**
   * 验证短信验证码
   *
   * @param configId 配置id
   * @param id 发送id
   * @param code 验证码
   * @return {boolean}
   */
  public boolean validateResponseForID(String configId, String id, String code) {
    CaptchaConfig config = this.captchaConfigService.get(configId);
    // 配置信息不存在
    if (config == null) {
      if (log.isDebugEnabled()) {
        log.debug("configId:" + configId + "\t 对应的配置信息没有找到!");
      }
      throw new ValidationException("200104", "configId:" + configId + "\t 对应的配置信息没有找到!");
    }
    Optional<Captcha> captchaOptional =
        captchaDao.findOne(
            PropertyFilter.builder().equal("config.id", configId).equal("sessionId", id).build());
    // 验证码不存在或者超过重试次数
    if (!captchaOptional.isPresent() || captchaOptional.get().getRetry() >= config.getRetry()) {
      throw new ValidationException("200105", "验证码不存在或者超过重试次数");
    }
    Captcha captcha = captchaOptional.get();
    // 验证码已经过期
    if (DateUtil.interval(DateUtil.now(), captcha.getUpdatedAt(), Calendar.SECOND)
        > config.getExpires()) {
      throw new ValidationException("200106", " 验证码已过期,请重新发送");
    }
    // 验证码匹配错误
    if (!code.equalsIgnoreCase(captcha.getValue())) {
      captcha.setRetry(captcha.getRetry() + 1);
      captchaDao.save(captcha);
      throw new ValidationException("200107", "验证码匹配错误，请重新发送");
    }
    captchaDao.delete(captcha);
    return true;
  }

  /**
   * 发送短信验证码
   *
   * @param configId 配置ID
   * @param id 发送id
   * @param phone 手机号
   * @param force 强制发送
   * @return String
   */
  public String getChallengeForID(String configId, String id, String phone, boolean force)
      throws ParseException {
    if (!RegexpUtil.isMatch(phone, VALIDATOR_MOBILE)) {
      if (log.isDebugEnabled()) {
        log.debug("发送的手机号码格式不对:" + phone);
      }
      throw new IgnoreException("发送的手机号码格式不对:" + phone);
    }
    CaptchaConfig config = this.captchaConfigService.get(configId);
    if (config == null) {
      if (log.isDebugEnabled()) {
        log.debug("configId:" + configId + "\t 对应的配置信息没有找到!");
      }
      throw new ValidationException("短信验证设置[id=" + configId + "]不存在!");
    }

    if (!force) {
      Date now = new Date();
      Date date = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));
      // 短信验证码 ：使用同一个签名，对同一个手机号码发送短信验证码，1条/分钟，5条/小时，累计10条/天
      List<ShortMessage> shortMessages =
          this.messageService.findList(
              phone,
              config.getTemplate().getId(),
              new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

      int size = shortMessages.size();
      Date[] time = new Date[size];
      for (int i = 0; i < size; i++) {
        time[i] = new Date();
        time[i] = shortMessages.get(i).getCreatedAt();
      }
      checkSmsSend(time, now);
    }

    Optional<Captcha> captchaOptional =
        captchaDao.findOne(
            PropertyFilter.builder().equal("config.id", configId).equal("sessionId", id).build());
    Captcha captcha = captchaOptional.orElse(null);

    if (captcha != null) {
      // 如果验证码超过有效期限
      boolean overdue =
          DateUtil.interval(DateUtil.now(), captcha.getUpdatedAt(), Calendar.SECOND)
              > config.getActive();
      // 验证码尝试次数已经超过允许次数
      boolean retryTimesExceeded = captcha.getRetry() >= config.getRetry();
      if (overdue || retryTimesExceeded || force) {
        captcha.setValue(
            captchaConfigService
                .getWordGenerator(config.getRandomWord())
                .getWord(config.getWordLength()));
        captcha.setRetry(0);
      }
    } else {

      captcha = new Captcha();
      captcha.setPhone(phone);
      captcha.setSessionId(id);
      captcha.setConfig(config);
      captcha.setValue(
          captchaConfigService
              .getWordGenerator(config.getRandomWord())
              .getWord(config.getWordLength()));
    }
    captchaDao.save(captcha);
    this.messageService.save(captcha);
    return captcha.getValue();
  }

  public void checkSmsSend(Date[] time, Date now) {
    if (time.length == 0) {
      return;
    }
    // 记录大于10  无法发送
    if (time.length >= 10) {
      log.error("200103验证码每天发送次数超限");
      throw new ValidationException("200103", "验证码每天发送次数超限");
    }
    // 一个小时内大于5 无法发送
    if (time.length >= 5 && (now.getTime() - time[4].getTime()) / (1000 * 60) <= 60) {
      log.error("200102验证码每小时发送次数超限");
      throw new ValidationException("200102", "验证码每小时发送次数超限");
    }
    // 当前时间跟最新记录时间是否相差一分钟
    long l = (now.getTime() - time[0].getTime()) / 1000;
    if (l <= 60) {
      log.error("200101验证码每分钟发送次数超限");
      throw new ValidationException("200101", "验证码每分钟发送次数超限");
    }
  }
}
