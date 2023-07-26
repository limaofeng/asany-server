package cn.asany.security.core.dao;

import cn.asany.security.core.domain.PermissionPolicy;
import org.jfantasy.framework.dao.jpa.JpaRepository;

/**
 * 权限策略
 *
 * @author limaofeng
 */
public interface PermissionPolicyDao extends JpaRepository<PermissionPolicy, Long> {}
