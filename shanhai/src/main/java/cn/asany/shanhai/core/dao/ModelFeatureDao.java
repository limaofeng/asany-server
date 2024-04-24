package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelFeature;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 实体特征
 *
 * @author limaofeng
 */
@Repository
public interface ModelFeatureDao extends AnyJpaRepository<ModelFeature, String> {}
