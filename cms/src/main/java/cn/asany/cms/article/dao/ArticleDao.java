package cn.asany.cms.article.dao;

import cn.asany.cms.article.bean.Article;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDao extends JpaRepository<Article, Long> {}
