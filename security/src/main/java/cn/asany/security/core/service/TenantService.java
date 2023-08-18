package cn.asany.security.core.service;

import cn.asany.security.core.dao.TenantDao;
import cn.asany.security.core.domain.Tenant;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

  private final TenantDao tenantDao;

  public TenantService(TenantDao tenantDao) {
    this.tenantDao = tenantDao;
  }

  public Page<Tenant> findPage(Pageable pageable, PropertyFilter filter) {
    return this.tenantDao.findPage(pageable, filter);
  }

  public Tenant save(Tenant tenant) {
    return this.tenantDao.save(tenant);
  }

  public Optional<Tenant> findById(String id) {
    return this.tenantDao.findById(id);
  }

  public Tenant update(String id, Tenant tenant, boolean merge) {
    tenant.setId(id);
    return this.tenantDao.update(tenant, merge);
  }

  public void delete(String id) {
    this.tenantDao.deleteById(id);
  }
}
