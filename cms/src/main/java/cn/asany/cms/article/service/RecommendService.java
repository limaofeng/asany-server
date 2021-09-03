package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Recommend;
import cn.asany.cms.article.dao.RecommendDao;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @Description @Author ChenWenJie @Data 2020/10/22 11:17 上午 */
@Service
@Transactional
public class RecommendService {
  @Autowired public RecommendDao recommendDao;

  /**
   * 根据查询条件查询所有推荐位
   *
   * @param filters
   * @return
   */
  public List<Recommend> findAll(List<PropertyFilter> filters, Sort sort) {
    return recommendDao.findAll(filters, sort);
  }

  public Recommend findById(Long id) {
    return recommendDao.findById(id).orElse(null);
  }

  public Recommend createRecommend(Recommend recommend) {
    return recommendDao.save(recommend);
  }

  public Recommend updateRecommend(Long id, boolean merge, Recommend recommend) {
    recommend.setId(id);
    return recommendDao.update(recommend, merge);
  }

  public void deleteRecommend(Long id) {
    recommendDao.deleteById(id);
  }
}
