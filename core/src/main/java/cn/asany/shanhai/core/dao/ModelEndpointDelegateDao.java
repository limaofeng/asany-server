package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.ModelEndpoint;
import org.jfantasy.framework.dao.jpa.JpaRepository;

/**
 * 实体接口委托
 *
 * @author limaofeng
 */
public interface ModelEndpointDelegateDao extends JpaRepository<ModelEndpoint, Long> {
}
