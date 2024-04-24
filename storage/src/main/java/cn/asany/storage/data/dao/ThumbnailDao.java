package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.Thumbnail;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailDao extends AnyJpaRepository<Thumbnail, Long> {}
