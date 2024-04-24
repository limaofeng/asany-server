package cn.asany.cms.special.service;

import cn.asany.cms.special.dao.SpecialArticleDao;
import cn.asany.cms.special.dao.SpecialDao;
import cn.asany.cms.special.dao.SubscriberDao;
import cn.asany.cms.special.domain.Special;
import cn.asany.cms.special.domain.SpecialArticle;
import cn.asany.cms.special.domain.Subscriber;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecialService {

  private final SpecialDao specialDao;
  private final SpecialArticleDao specialArticleDao;
  private final SubscriberDao subscriberDao;

  @Autowired
  public SpecialService(
      SpecialDao specialDao, SpecialArticleDao specialArticleDao, SubscriberDao subscriberDao) {
    this.specialDao = specialDao;
    this.specialArticleDao = specialArticleDao;
    this.subscriberDao = subscriberDao;
  }

  @Transactional
  public Page<Special> findPage(Pageable pageable, PropertyFilter filter) {
    return this.specialDao.findPage(pageable, filter);
  }

  @Transactional
  public Special update(Special special, boolean patch) {
    return this.specialDao.update(special, patch);
  }

  public Special get(String id) {
    return this.specialDao.getReferenceById(id);
  }

  @Transactional
  public Special save(Special special) {
    return this.specialDao.save(special);
  }

  @Transactional
  public void delete(String id) {
    this.specialDao.deleteById(id);
  }

  @Transactional
  public Page<Subscriber> findSubscriberPager(String id, Pageable pageable, PropertyFilter filter) {
    filter.equal("special.id", id);
    return this.subscriberDao.findPage(pageable, filter);
  }

  @Transactional
  public Page<SpecialArticle> findSpecialArticlePager(
      String id, Pageable pageable, PropertyFilter filter) {
    filter.equal("special.id", id);
    return this.specialArticleDao.findPage(pageable, filter);
  }

  @Transactional
  public Subscriber save(Subscriber subscriber) {
    return this.subscriberDao.save(subscriber);
  }

  @Transactional
  public SpecialArticle save(SpecialArticle article) {
    return this.specialArticleDao.save(article);
  }
}
