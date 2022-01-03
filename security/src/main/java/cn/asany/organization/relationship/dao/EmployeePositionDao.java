package cn.asany.organization.relationship.dao;

import cn.asany.organization.relationship.bean.EmployeePosition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-04-12 14:26
 */
@Repository
public interface EmployeePositionDao extends JpaRepository<EmployeePosition, Long> {
  /**
   * 删除人员职位对应关系 - 根据组织 ID
   *
   * @param orgId 组织 ID
   */
  @Modifying
  @Query(
      nativeQuery = true,
      value = "DELETE FROM org_employee_position e WHERE e.organization_id = :orgId")
  void deleteByOrgId(@org.springframework.data.repository.query.Param("orgId") Long orgId);
}
