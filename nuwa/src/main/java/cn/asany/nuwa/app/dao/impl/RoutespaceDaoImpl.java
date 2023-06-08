package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.dao.RoutespaceDao;
import cn.asany.nuwa.app.domain.Routespace;
import java.util.List;
import java.util.Optional;
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
  public List<Routespace> findByIdsWithApplicationTemplate(Set<String> ids) {
    return this.findAll(PropertyFilter.newFilter().in("id", ids));
  }

  @Override
  public Optional<Routespace> findByIdWithApplicationTemplate(String id) {
    return this.findOne(PropertyFilter.newFilter().endsWith("id", id));
  }
}
