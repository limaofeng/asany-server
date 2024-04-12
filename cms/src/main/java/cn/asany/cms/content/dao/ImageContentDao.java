package cn.asany.cms.content.dao;

import cn.asany.cms.content.domain.ImageContent;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageContentDao extends JpaRepository<ImageContent, Long> {}
