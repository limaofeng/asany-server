package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.dao.ApplicationDao;
import cn.asany.nuwa.app.domain.Application;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

/**
 * 应用 数据存储类
 *
 * @author limaofeng
 */
public class ApplicationDaoImpl extends SimpleAnyJpaRepository<Application, Long>
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
