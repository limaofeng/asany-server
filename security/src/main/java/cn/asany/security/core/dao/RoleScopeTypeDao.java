package cn.asany.security.core.dao;

import cn.asany.security.core.domain.RoleScopeType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 角色使用范围类型
 *
 * @author liumeng
 */
@Repository
public interface RoleScopeTypeDao extends JpaRepository<RoleScopeType, String> {}
