package cn.asany.organization.employee.dao;

import cn.asany.organization.core.domain.EmployeeStatus;
import cn.asany.organization.core.domain.Organization;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatusDao extends JpaRepository<EmployeeStatus, Long> {

  EmployeeStatus findByCodeAndOrganization(String code, Organization organization);

  EmployeeStatus findByIsDefaultAndOrganization(Boolean isdefault, Organization organization);
}
