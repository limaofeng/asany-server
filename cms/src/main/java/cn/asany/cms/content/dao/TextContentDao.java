package cn.asany.cms.content.dao;

import cn.asany.cms.content.domain.TextContent;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextContentDao extends AnyJpaRepository<TextContent, Long> {}
