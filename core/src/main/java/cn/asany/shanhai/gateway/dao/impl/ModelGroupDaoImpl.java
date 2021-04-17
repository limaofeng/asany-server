package cn.asany.shanhai.gateway.dao.impl;

import cn.asany.shanhai.gateway.bean.ModelGroup;
import cn.asany.shanhai.gateway.dao.ModelGroupDao;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.List;

public class ModelGroupDaoImpl extends ComplexJpaRepository<ModelGroup, Long> implements ModelGroupDao {

    public ModelGroupDaoImpl(EntityManager entityManager) {
        super(ModelGroup.class, entityManager);
    }

    @Override
    public List<ModelGroup> findAllWithItems() {
        return this.findAll(Sort.by(Sort.Direction.ASC, "sort"));
    }

}