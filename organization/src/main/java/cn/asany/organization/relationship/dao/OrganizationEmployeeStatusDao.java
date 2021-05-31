package cn.asany.organization.relationship.dao;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.bean.OrganizationEmployeeStatus;
import org.apache.ibatis.annotations.Param;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationEmployeeStatusDao extends JpaRepository<OrganizationEmployeeStatus,Long> {
    OrganizationEmployeeStatus findByCodeAndOrganization(String code, Organization organization);
    OrganizationEmployeeStatus findByIsDefaultAndOrganization(Boolean isdefault, Organization organization);

    @Query(nativeQuery = true, value = " select * from org_organization_employee_status where organization_id=:organizationId and name=:status")
    List<OrganizationEmployeeStatus> findByNameAndOrganization(@Param("status") String status, @Param("organizationId") String organizationId);
}
