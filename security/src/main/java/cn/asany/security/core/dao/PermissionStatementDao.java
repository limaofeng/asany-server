package cn.asany.security.core.dao;

import cn.asany.security.core.domain.PermissionStatement;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 权限声明
 *
 * @author limaofeng
 */
@Repository
public interface PermissionStatementDao extends AnyJpaRepository<PermissionStatement, Long> {}
