package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.ArticleStoreTemplate;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleStoreTemplateDao extends AnyJpaRepository<ArticleStoreTemplate, Long> {}
