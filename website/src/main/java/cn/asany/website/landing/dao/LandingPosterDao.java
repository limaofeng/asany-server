package cn.asany.website.landing.dao;

import cn.asany.website.landing.domain.LandingPoster;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandingPosterDao extends AnyJpaRepository<LandingPoster, Long> {}
