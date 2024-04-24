package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.Department;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 部门管理
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface DepartmentDao extends AnyJpaRepository<Department, Long> {}
