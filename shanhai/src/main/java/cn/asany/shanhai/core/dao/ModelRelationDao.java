package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.ModelRelation;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRelationDao extends JpaRepository<ModelRelation, Long> {
}
