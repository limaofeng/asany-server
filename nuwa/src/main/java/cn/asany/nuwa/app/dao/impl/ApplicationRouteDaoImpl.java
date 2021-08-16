package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.dao.ApplicationRouteDao;
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
  public List<ApplicationRoute> findAllByApplicationAndSpaceWithComponent(
      Long applicationId, String space) {
    return this.findAll(
        PropertyFilter.builder()
            .equal("application.id", applicationId)
            .equal("space.id", space)
            .build());
  }
}
