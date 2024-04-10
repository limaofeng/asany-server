package cn.asany.cms.content.dao;

import cn.asany.cms.content.domain.DocumentContent;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentContentDao extends JpaRepository<DocumentContent, Long> {}
