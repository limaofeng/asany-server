package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.EmployeeGroup;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户组
 *
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface EmployeeGroupDao extends AnyJpaRepository<EmployeeGroup, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT * FROM org_employee_group where locate(:name, `name`)>0 and scope = (SELECT id FROM org_employee_group_scope where id= :scope AND organization_id= :organization )")
  List<EmployeeGroup> findEmployeeGroups(
      @Param("name") Long name,
      @Param("scope") String scope,
      @Param("organization") String organization);
}
