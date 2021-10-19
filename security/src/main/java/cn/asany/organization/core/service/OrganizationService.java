package cn.asany.organization.core.service;

import cn.asany.organization.core.bean.EmployeeStatus;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.bean.OrganizationDimension;
import cn.asany.organization.core.bean.enums.MemberRole;
import cn.asany.organization.core.dao.OrganizationDao;
import cn.asany.organization.core.dao.OrganizationDimensionDao;
import cn.asany.organization.relationship.dao.EmployeeStatusDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final EmployeeStatusDao employeeStatusDao;
  private final OrganizationDimensionDao organizationDimensionDao;

  public OrganizationService(
      OrganizationDao organizationDao,
      EmployeeStatusDao employeeStatusDao,
      OrganizationDimensionDao organizationDimensionDao) {
    this.organizationDao = organizationDao;
    this.employeeStatusDao = employeeStatusDao;
    this.organizationDimensionDao = organizationDimensionDao;
  }

  public Organization save(Organization organization) {
    organization.setDimensions(new ArrayList<>());
    this.organizationDao.save(organization);

    OrganizationDimension dimension =
        OrganizationDimension.builder()
            .code(Organization.DEFAULT_DIMENSION)
            .name("成员")
            .organization(organization)
            .build();

    this.organizationDimensionDao.save(dimension);

    List<EmployeeStatus> statuses = new ArrayList<>();
    statuses.add(
        EmployeeStatus.builder()
            .organization(organization)
            .dimension(dimension)
            .code(MemberRole.ADMIN.getValue())
            .name("管理员")
            .build());
    statuses.add(
        EmployeeStatus.builder()
            .organization(organization)
            .dimension(dimension)
            .code(MemberRole.USER.getValue())
            .name("成员")
            .build());

    for (EmployeeStatus status : statuses) {
      this.employeeStatusDao.save(status);
    }

    dimension.setStatuses(statuses);

    organization.getDimensions().add(dimension);

    return organization;
  }

  public Optional<Organization> findById(Long id) {
    return organizationDao.findById(id);
  }

  public Organization getById(Long id) {
    return organizationDao.getById(id);
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
