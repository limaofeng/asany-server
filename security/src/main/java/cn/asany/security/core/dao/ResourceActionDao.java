package cn.asany.security.core.dao;

import cn.asany.security.core.domain.ResourceAction;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 操作
 *
 * @author limaofeng
 */
@Repository
public interface ResourceActionDao extends JpaRepository<ResourceAction, String> {}
