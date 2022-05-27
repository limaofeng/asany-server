package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.HtmlContent;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HtmlContentDao extends JpaRepository<HtmlContent, Long> {}
