package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleMetaField;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMetaFieldDao extends AnyJpaRepository<ArticleMetaField, Long> {}
