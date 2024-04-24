package cn.asany.ui.resources.dao;

import cn.asany.ui.resources.domain.Component;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 组件 Dao
 *
 * @author limaofeng
 */
@Repository
public interface ComponentDao extends AnyJpaRepository<Component, Long> {}
