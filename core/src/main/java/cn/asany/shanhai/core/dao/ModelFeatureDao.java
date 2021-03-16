package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.ModelFeature;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelFeatureDao extends JpaRepository<ModelFeature, String> {
}
