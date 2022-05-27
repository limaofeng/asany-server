package cn.asany.cms.special.dao;

import cn.asany.cms.special.domain.SpecialArticle;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialArticleDao extends JpaRepository<SpecialArticle, Long> {}
