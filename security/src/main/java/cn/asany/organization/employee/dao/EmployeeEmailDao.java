package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.EmployeeEmail;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 邮箱
 *
 * @author limaofeng
 */
public interface EmployeeEmailDao extends JpaRepository<EmployeeEmail, Long> {
  /**
   * 删除员工 - 根据组织 ID
   *
   * @param orgId 组织 ID
   */
  @Modifying
  @Query(
      nativeQuery = true,
      value =
          "DELETE FROM org_employee_email e WHERE e.employee_id in (SELECT id FROM org_employee WHERE organization_id = :orgId)")
  void deleteByOrgId(@Param("orgId") Long orgId);
}
