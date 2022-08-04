package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleFeature;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 文章特征
 *
 * @author ChenWenJie
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface ArticleFeatureDao extends JpaRepository<ArticleFeature, Long> {}
