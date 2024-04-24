package cn.asany.website.landing.dao.impl;

import cn.asany.website.landing.dao.LandingPageDao;
import cn.asany.website.landing.domain.LandingPage;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

public class LandingPageDaoImpl extends SimpleAnyJpaRepository<LandingPage, Long>
    implements LandingPageDao {
  public LandingPageDaoImpl(EntityManager entityManager) {
    super(LandingPage.class, entityManager);
  }

  @Override
  public Optional<LandingPage> findByIdWithPosterAndStores(Long id) {
    return this.findById(id);
  }
}
