package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.dao.ApplicationRouteDao;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import java.util.List;
import javax.persistence.EntityManager;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

public class ApplicationRouteDaoImpl extends ComplexJpaRepository<ApplicationRoute, Long>
    implements ApplicationRouteDao {

  public ApplicationRouteDaoImpl(EntityManager entityManager) {
    super(ApplicationRoute.class, entityManager);
  }

  @Override
  public List<ApplicationRoute> findAllByApplicationWithComponent(Long applicationId) {
    return this.findAll(PropertyFilter.newFilter().equal("application.id", applicationId));
  }
}
