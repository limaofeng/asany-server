package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Feature;
import cn.asany.cms.article.dao.FeatureDao;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 推荐位 / 特征
 *
 * @author ChenWenJie
 * @date 2020/10/22 11:17 上午
 */
@Service
@Transactional
public class FeatureService {

  @Autowired public FeatureDao featureDao;

  /**
   * 根据查询条件查询所有推荐位
   *
   * @param filters
   * @return
   */
  public List<Feature> findAll(List<PropertyFilter> filters, Sort sort) {
    return featureDao.findAll(filters, sort);
  }

  public Feature findById(Long id) {
    return featureDao.findById(id).orElse(null);
  }

  public Feature createFeature(Feature feature) {
    return featureDao.save(feature);
  }

  public Feature updateFeature(Long id, boolean merge, Feature feature) {
    feature.setId(id);
    return featureDao.update(feature, merge);
  }

  public void deleteFeature(Long id) {
    featureDao.deleteById(id);
  }

  public Optional<Feature> findByCode(String code) {
    return this.featureDao.findBy("code", code);
  }
}
