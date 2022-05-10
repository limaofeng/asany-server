package cn.asany.website.landing.dao.impl;

import cn.asany.website.landing.bean.LandingPage;
import cn.asany.website.landing.dao.LandingPageDao;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;

public class LandingPageDaoImpl extends ComplexJpaRepository<LandingPage, Long>
    implements LandingPageDao {
  public LandingPageDaoImpl(EntityManager entityManager) {
    super(LandingPage.class, entityManager);
  }

  @Override
  public Optional<LandingPage> findByIdWithPosterAndStores(Long id) {
    return this.findById(id);
  }
}
