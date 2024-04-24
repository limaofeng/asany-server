package cn.asany.organization.employee.dao;

import cn.asany.organization.core.domain.EmployeeStatus;
import cn.asany.organization.core.domain.Organization;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatusDao extends AnyJpaRepository<EmployeeStatus, Long> {

  EmployeeStatus findByCodeAndOrganization(String code, Organization organization);

  EmployeeStatus findByIsDefaultAndOrganization(Boolean isdefault, Organization organization);
}
