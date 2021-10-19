package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.EmployeeIdentity;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 员工身份
 *
 * @author limaofeng
 */
@Repository
public interface EmployeeIdentityDao extends JpaRepository<EmployeeIdentity, Long> {}
