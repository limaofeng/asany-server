package cn.asany.website.landing.dao;

import cn.asany.website.landing.bean.LandingPage;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandingPageDao extends JpaRepository<LandingPage, Long> {}
