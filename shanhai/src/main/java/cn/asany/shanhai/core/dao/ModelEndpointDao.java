package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelEndpoint;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 实体接口
 *
 * @author limaofeng
 */
@Repository
public interface ModelEndpointDao extends JpaRepository<ModelEndpoint, Long> {}
