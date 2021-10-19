package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.bean.Employee;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 员工
 *
 * @author 李茂峰
 * @data 2019/3/5 15:33
 */
@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {}
