package cn.asany.organization.relationship.dao;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.bean.EmployeeStatus;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationEmployeeStatusDao
    extends JpaRepository<EmployeeStatus, Long> {

  EmployeeStatus findByCodeAndOrganization(String code, Organization organization);

  EmployeeStatus findByIsDefaultAndOrganization(
      Boolean isdefault, Organization organization);
}
