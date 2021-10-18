package cn.asany.organization.core.service;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.dao.OrganizationDao;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 组织服务
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-24 17:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrganizationService {

  private final OrganizationDao organizationDao;

  public OrganizationService(OrganizationDao organizationDao) {
    this.organizationDao = organizationDao;
  }

  public Organization save(Organization organization) {
    return this.organizationDao.save(organization);
  }

  public Optional<Organization> get(Long id) {
    return organizationDao.findById(id);
  }

  public Organization update(Long id, boolean merge, Organization department) {
    department.setId(id);
    return this.organizationDao.update(department, merge);
  }

  public void delete(Long id) {
    this.organizationDao.deleteById(id);
  }

  public List<Organization> findAll(List<PropertyFilter> filters) {
    return this.organizationDao.findAll(
        filters, new OrderBy("updatedAt", OrderBy.Direction.DESC).toSort());
  }

    public Optional<Organization> findOneByCode(String code) {
        return this.organizationDao.findOneBy("code", code);
    }
}
