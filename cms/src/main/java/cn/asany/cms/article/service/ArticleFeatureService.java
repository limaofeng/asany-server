package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.ArticleFeature;
import cn.asany.cms.article.dao.ArticleFeatureDao;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.StringUtil;
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
public class ArticleFeatureService {

  public final ArticleFeatureDao articleFeatureDao;

  public ArticleFeatureService(ArticleFeatureDao articleFeatureDao) {
    this.articleFeatureDao = articleFeatureDao;
  }

  /**
   * 根据查询条件查询所有推荐位
   *
   * @param filters
   * @return
   */
  public List<ArticleFeature> findAll(List<PropertyFilter> filters, Sort sort) {
    return articleFeatureDao.findAll(filters, sort);
  }

  public ArticleFeature findById(Long id) {
    return articleFeatureDao.findById(id).orElse(null);
  }

  @Transactional
  public ArticleFeature createArticleFeature(ArticleFeature feature) {
    if (feature.getNeedReview() == null) {
      feature.setNeedReview(false);
    }
    if (StringUtil.isBlank(feature.getCode())) {
      feature.setCode(PinyinUtils.getAll(feature.getName()));
    }
    return articleFeatureDao.save(feature);
  }

  @Transactional
  public ArticleFeature updateArticleFeature(Long id, boolean merge, ArticleFeature feature) {
    feature.setId(id);
    return articleFeatureDao.update(feature, merge);
  }

  @Transactional
  public void deleteArticleFeature(Long id) {
    articleFeatureDao.deleteById(id);
  }

  public Optional<ArticleFeature> findByCode(String code) {
    return this.articleFeatureDao.findBy("code", code);
  }
}
