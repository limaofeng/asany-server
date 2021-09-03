package cn.asany.cms.article.dao;

import cn.asany.cms.article.bean.Content;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDao extends JpaRepository<Content, Long> {}
