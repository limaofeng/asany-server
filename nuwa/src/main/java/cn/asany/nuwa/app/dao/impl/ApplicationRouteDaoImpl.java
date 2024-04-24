package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.dao.ApplicationRouteDao;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import jakarta.persistence.EntityManager;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

public class ApplicationRouteDaoImpl extends SimpleAnyJpaRepository<ApplicationRoute, Long>
    implements ApplicationRouteDao {

  public ApplicationRouteDaoImpl(EntityManager entityManager) {
    super(ApplicationRoute.class, entityManager);
  }

  @Override
  public List<ApplicationRoute> findAllByApplicationWithComponent(Long applicationId) {
    return this.findAll(PropertyFilter.newFilter().equal("application.id", applicationId));
  }
}
