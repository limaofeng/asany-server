package cn.asany.cms.content.dao;

import cn.asany.cms.content.domain.VideoContent;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoContentDao extends JpaRepository<VideoContent, Long> {}
