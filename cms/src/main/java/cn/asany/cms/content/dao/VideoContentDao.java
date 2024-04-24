package cn.asany.cms.content.dao;

import cn.asany.cms.content.domain.VideoContent;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoContentDao extends AnyJpaRepository<VideoContent, Long> {}
