package cn.asany.sms.service;

import cn.asany.sms.dao.CaptchaConfigDao;
import cn.asany.sms.dao.CaptchaDao;
import cn.asany.sms.domain.Captcha;
import cn.asany.sms.domain.CaptchaConfig;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CaptchaService {

  private static final Log logger = LogFactory.getLog(CaptchaService.class);

  private final CaptchaConfigDao captchaConfigDao;
  private final CaptchaDao captchaDao;

  private final ConcurrentMap<String, RandomWordGenerator> wordGeneratorCache =
      new ConcurrentHashMap<>();

  @Autowired
  public CaptchaService(CaptchaConfigDao captchaConfigDao, CaptchaDao captchaDao) {
    this.captchaConfigDao = captchaConfigDao;
    this.captchaDao = captchaDao;
  }

  RandomWordGenerator getWordGenerator(String randomWord) {
    if (!wordGeneratorCache.containsKey(randomWord)) {
      if (logger.isDebugEnabled()) {
        logger.debug("缓存验证码生成器:" + randomWord);
      }
      wordGeneratorCache.put(randomWord, new RandomWordGenerator(randomWord));
    }
    return wordGeneratorCache.get(randomWord);
  }

  protected RandomWordGenerator removeWordGenerator(String randomWord) {
    return wordGeneratorCache.remove(randomWord);
  }

  public Page<Captcha> findCaptchaPage(Pageable page, List<PropertyFilter> filters) {
    return this.captchaDao.findPage(page, filters);
  }

  public CaptchaConfig get(String configId) {
    return captchaConfigDao.findById(configId).orElse(null);
  }

  /**
   * 获取所有短信配置
   *
   * @return pager
   */
  public Page<CaptchaConfig> findPage(Pageable page, List<PropertyFilter> filters) {
    return this.captchaConfigDao.findPage(page, filters);
  }

  /**
   * 保存短信配置
   *
   * @param captchaConfig 配置
   * @return 配置
   */
  public CaptchaConfig save(CaptchaConfig captchaConfig) {
    return this.captchaConfigDao.save(captchaConfig);
  }

  /**
   * 删除配置
   *
   * @param ids 配置ID
   */
  public void delete(String... ids) {
    this.captchaConfigDao.deleteAllByIdInBatch(Arrays.asList(ids));
  }
}
