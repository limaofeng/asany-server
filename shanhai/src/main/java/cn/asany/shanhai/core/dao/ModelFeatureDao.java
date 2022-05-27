package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelFeature;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 实体特征
 *
 * @author limaofeng
 */
@Repository
public interface ModelFeatureDao extends JpaRepository<ModelFeature, String> {}
