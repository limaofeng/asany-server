package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.bean.EmployeePhoneNumber;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-17 15:52
 */
@Repository
public interface EmployeePhoneDao extends JpaRepository<EmployeePhoneNumber, Long> {}
