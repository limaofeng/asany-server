package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.bean.EmployeeEmail;
import org.jfantasy.framework.dao.jpa.JpaRepository;

public interface EmployeeMailDao extends JpaRepository <EmployeeEmail,Long> {
}
