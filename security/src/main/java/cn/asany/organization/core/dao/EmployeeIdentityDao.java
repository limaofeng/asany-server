package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.EmployeeIdentity;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工身份
 *
 * @author limaofeng
 */
@Repository
public interface EmployeeIdentityDao extends JpaRepository<EmployeeIdentity, Long> {
  /**
   * 删除员工 - 根据组织 ID
   *
   * @param orgId 组织 ID
   */
  @Modifying
  @Query(
      nativeQuery = true,
      value = "DELETE FROM org_employee_identity e WHERE e.organization_id = :orgId")
  void deleteByOrgId(@Param("orgId") Long orgId);
}
