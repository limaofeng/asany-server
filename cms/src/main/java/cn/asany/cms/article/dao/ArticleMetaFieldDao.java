package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleMetaField;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMetaFieldDao extends JpaRepository<ArticleMetaField, Long> {}
