package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.dao.ApplicationDao;
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
    return this.findOne(PropertyFilter.builder().equal("clientId", clientId).build());
  }

  @Override
  public Optional<Application> findDetailsById(Long id) {
    return this.findOne(PropertyFilter.builder().equal("id", id).build());
  }
}
