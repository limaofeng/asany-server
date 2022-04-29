package cn.asany.website.landing.dao;

import cn.asany.website.landing.bean.LandingStore;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandingStoreDao extends JpaRepository<LandingStore, Long> {}
