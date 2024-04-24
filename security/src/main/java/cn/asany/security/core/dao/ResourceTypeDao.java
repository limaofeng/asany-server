package cn.asany.security.core.dao;

import cn.asany.security.core.domain.ResourceType;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 资源类型
 *
 * @author limaofeng
 */
@Repository
public interface ResourceTypeDao extends AnyJpaRepository<ResourceType, String> {}
