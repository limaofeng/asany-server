package cn.asany.ui.resources.dao;

import cn.asany.ui.resources.domain.Component;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 组件 Dao
 *
 * @author limaofeng
 */
@Repository
public interface ComponentDao extends JpaRepository<Component, Long> {}
