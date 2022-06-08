package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleStoreTemplate;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleStoreTemplateDao extends JpaRepository<ArticleStoreTemplate, Long> {}
