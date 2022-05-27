package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.Thumbnail;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailDao extends JpaRepository<Thumbnail, Long> {}
