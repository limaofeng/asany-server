package cn.asany.cms.special.dao;

import cn.asany.cms.special.domain.SpecialArticle;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialArticleDao extends AnyJpaRepository<SpecialArticle, Long> {}
