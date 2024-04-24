package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleFeature;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 文章特征
 *
 * @author ChenWenJie
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface ArticleFeatureDao extends AnyJpaRepository<ArticleFeature, Long> {}
