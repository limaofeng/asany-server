package cn.asany.security.core.dao;

import cn.asany.security.core.domain.ResourceType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 资源类型
 *
 * @author limaofeng
 */
@Repository
public interface ResourceTypeDao extends JpaRepository<ResourceType, String> {}
