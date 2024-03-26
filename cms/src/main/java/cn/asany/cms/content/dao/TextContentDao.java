package cn.asany.cms.content.dao;

import cn.asany.cms.content.domain.TextContent;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextContentDao extends JpaRepository<TextContent, Long> {}
