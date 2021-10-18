package cn.asany.organization.employee.dao;

import cn.asany.organization.relationship.bean.EmployeePosition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-04-12 14:26
 */
@Repository
public interface EmployeePositionDao extends JpaRepository<EmployeePosition, Long> {}
