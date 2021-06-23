package cn.asany.organization.relationship.dao;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.bean.OrganizationEmployeeStatus;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationEmployeeStatusDao extends JpaRepository<OrganizationEmployeeStatus,Long> {

    OrganizationEmployeeStatus findByCodeAndOrganization(String code, Organization organization);

    OrganizationEmployeeStatus findByIsDefaultAndOrganization(Boolean isdefault, Organization organization);

}
