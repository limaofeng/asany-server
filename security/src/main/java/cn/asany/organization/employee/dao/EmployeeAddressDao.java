package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.domain.EmployeeAddress;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-17 15:52
 */
@Repository
public interface EmployeeAddressDao extends JpaRepository<EmployeeAddress, Long> {

  public List<EmployeeAddress> findByEmployeeAndLabel(Employee employee, String label);

  public List<EmployeeAddress> findByEmployee(Employee employee);
}
