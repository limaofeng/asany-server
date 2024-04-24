package cn.asany.system.dao;

import cn.asany.system.domain.ShortLink;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortLinkDao extends AnyJpaRepository<ShortLink, Long> {}
