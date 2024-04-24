package cn.asany.cms.content.dao;

import cn.asany.cms.content.domain.ImageContent;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageContentDao extends AnyJpaRepository<ImageContent, Long> {}
