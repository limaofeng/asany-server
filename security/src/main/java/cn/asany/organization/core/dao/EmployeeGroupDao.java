package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.EmployeeGroup;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户组
 *
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-17 15:52
 */
@Repository
public interface EmployeeGroupDao extends JpaRepository<EmployeeGroup, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT * FROM org_employee_group where locate(:name, `name`)>0 and scope = (SELECT id FROM org_employee_group_scope where id= :scope AND organization_id= :organization )")
  List<EmployeeGroup> findEmployeeGroups(
      @Param("name") Long name,
      @Param("scope") String scope,
      @Param("organization") String organization);
}
