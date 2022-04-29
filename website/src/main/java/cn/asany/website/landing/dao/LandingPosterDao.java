package cn.asany.website.landing.dao;

import cn.asany.website.landing.bean.LandingPoster;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandingPosterDao extends JpaRepository<LandingPoster, Long> {}
