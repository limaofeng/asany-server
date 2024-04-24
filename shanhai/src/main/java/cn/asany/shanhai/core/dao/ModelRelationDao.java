package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelRelation;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRelationDao extends AnyJpaRepository<ModelRelation, Long> {}
