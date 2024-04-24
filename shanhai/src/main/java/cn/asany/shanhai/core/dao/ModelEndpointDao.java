package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelEndpoint;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 实体接口
 *
 * @author limaofeng
 */
@Repository
public interface ModelEndpointDao extends AnyJpaRepository<ModelEndpoint, Long> {}
