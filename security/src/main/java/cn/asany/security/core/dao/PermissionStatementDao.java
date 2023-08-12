package cn.asany.security.core.dao;

import cn.asany.security.core.domain.PermissionStatement;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 权限声明
 *
 * @author limaofeng
 */
@Repository
public interface PermissionStatementDao extends JpaRepository<PermissionStatement, Long> {
}
