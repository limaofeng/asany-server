package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.domain.EmployeeLink;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface EmployeeLinkDao extends JpaRepository<EmployeeLink, Long> {

  EmployeeLink findByEmployee(@Param("employee") Employee employee);
}
