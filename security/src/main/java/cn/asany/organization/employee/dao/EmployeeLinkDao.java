package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.domain.EmployeeLink;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface EmployeeLinkDao extends AnyJpaRepository<EmployeeLink, Long> {

  EmployeeLink findByEmployee(@Param("employee") Employee employee);
}
