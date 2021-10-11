package cn.asany.cms.article.dao;

import cn.asany.cms.article.bean.ArticleFeature;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 文章特征
 *
 * @author ChenWenJie
 * @date 2020/10/23 6:06 下午
 */
@Repository
public interface ArticleFeatureDao extends JpaRepository<ArticleFeature, Long> {}
