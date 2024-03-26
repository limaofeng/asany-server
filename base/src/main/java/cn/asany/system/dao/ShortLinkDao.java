package cn.asany.system.dao;

import cn.asany.system.domain.ShortLink;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortLinkDao extends JpaRepository<ShortLink, Long> {}
