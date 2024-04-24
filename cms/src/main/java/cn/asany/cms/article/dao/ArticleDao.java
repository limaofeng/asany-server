package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.Article;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 文章
 *
 * @author limaofeng
 */
@Repository
public interface ArticleDao extends AnyJpaRepository<Article, Long> {}
