package cn.asany.cms.body.dao;

import cn.asany.cms.body.domain.Content;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDao extends JpaRepository<Content, Long> {}
