package cn.asany.organization.relationship.dao;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.employee.bean.Employee;
import cn.asany.organization.relationship.bean.OrganizationEmployee;
import cn.asany.organization.relationship.bean.Position;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationEmployeeDao  extends JpaRepository<OrganizationEmployee, String> {
    OrganizationEmployee findByEmployeeAndOrganization(Employee employee, Organization organization);

    OrganizationEmployee findByEmployeeAndOrganizationAndPosition(Employee employee, Organization organization, Position position);

}
