package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.bean.Routespace;
import cn.asany.nuwa.app.dao.RoutespaceDao;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

public class RoutespaceDaoImpl extends ComplexJpaRepository<Routespace, String>
    implements RoutespaceDao {

  public RoutespaceDaoImpl(EntityManager entityManager) {
    super(Routespace.class, entityManager);
  }

  @Override
  public List<Routespace> findByIdsWithApplicationTemplateRoute(Set<String> ids) {
    return this.findAll(PropertyFilter.builder().in("id", ids).build());
  }
}
