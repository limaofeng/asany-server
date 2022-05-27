package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.Article;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 文章
 *
 * @author limaofeng
 */
@Repository
public interface ArticleDao extends JpaRepository<Article, Long> {}
