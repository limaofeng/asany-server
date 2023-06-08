package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.dao.ApplicationDao;
import cn.asany.nuwa.app.domain.Application;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

/**
 * 应用 数据存储类
 *
 * @author limaofeng
 */
public class ApplicationDaoImpl extends ComplexJpaRepository<Application, Long>
    implements ApplicationDao {

  public ApplicationDaoImpl(EntityManager entityManager) {
    super(Application.class, entityManager);
  }

  @Override
  public Optional<Application> findDetailsByClientId(String clientId) {
    return this.findOne(PropertyFilter.newFilter().equal("clientId", clientId));
  }

  @Override
  public Optional<Application> findDetailsById(Long id) {
    return this.findOne(PropertyFilter.newFilter().equal("id", id));
  }

  @Override
  public Optional<Application> findOneWithRoutesByClientId(String clientId) {
    return this.findOne(PropertyFilter.newFilter().equal("clientId", clientId));
  }

  @Override
  public Optional<Application> findOneWithMenusByClientId(String clientId) {
    return this.findOne(PropertyFilter.newFilter().equal("clientId", clientId));
  }

  @Override
  public Optional<Application> findOneWithRoutesById(Long id) {
    return this.findOne(PropertyFilter.newFilter().equal("id", id));
  }

  @Override
  public Optional<Application> findOneWithMenusById(Long id) {
    return this.findOne(PropertyFilter.newFilter().equal("id", id));
  }

  @Override
  public Optional<Application> findOneWithClientDetails(PropertyFilter filter) {
    return this.findOne(filter);
  }
}
