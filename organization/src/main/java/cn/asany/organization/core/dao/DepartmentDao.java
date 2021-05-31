package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.Department;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 部门管理
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-03-11 14:41
 */
@Repository
public interface DepartmentDao extends JpaRepository<Department, Long> {

}
