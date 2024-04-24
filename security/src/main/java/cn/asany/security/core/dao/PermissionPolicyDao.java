package cn.asany.security.core.dao;

import cn.asany.security.core.domain.PermissionPolicy;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;

/**
 * 权限策略
 *
 * @author limaofeng
 */
public interface PermissionPolicyDao extends AnyJpaRepository<PermissionPolicy, Long> {}
