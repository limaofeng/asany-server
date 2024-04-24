package cn.asany.cms.content.dao;

import cn.asany.cms.content.domain.DocumentContent;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentContentDao extends AnyJpaRepository<DocumentContent, Long> {}
