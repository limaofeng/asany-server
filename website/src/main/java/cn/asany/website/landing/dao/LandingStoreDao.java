package cn.asany.website.landing.dao;

import cn.asany.website.landing.domain.LandingStore;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandingStoreDao extends AnyJpaRepository<LandingStore, Long> {}
