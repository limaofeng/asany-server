package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.EmployeePhoneNumber;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface EmployeePhoneDao extends JpaRepository<EmployeePhoneNumber, Long> {
  /**
   * 删除员工 - 根据组织 ID
   *
   * @param orgId 组织 ID
   */
  @Modifying
  @Query(
      nativeQuery = true,
      value =
          "DELETE FROM org_employee_phone_number e WHERE e.employee_id in (SELECT id FROM org_employee WHERE organization_id = :orgId)")
  void deleteByOrgId(@Param("orgId") Long orgId);
}
