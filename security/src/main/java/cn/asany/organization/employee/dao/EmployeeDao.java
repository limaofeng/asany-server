package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.Employee;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工
 *
 * @author 李茂峰
 */
@Repository
public interface EmployeeDao extends AnyJpaRepository<Employee, Long> {
  /**
   * 删除员工 - 根据组织 ID
   *
   * @param orgId 组织 ID
   */
  @Modifying
  @Query(nativeQuery = true, value = "DELETE FROM org_employee e WHERE e.organization_id = :orgId")
  void deleteByOrgId(@Param("orgId") Long orgId);
}
