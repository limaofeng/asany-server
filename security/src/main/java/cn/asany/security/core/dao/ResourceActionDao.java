package cn.asany.security.core.dao;

import cn.asany.security.core.domain.ResourceAction;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 操作
 *
 * @author limaofeng
 */
@Repository
public interface ResourceActionDao extends AnyJpaRepository<ResourceAction, String> {}
