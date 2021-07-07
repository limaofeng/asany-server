package cn.asany.shanhai.core.dao.impl;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.dao.ModelDao;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.IgnoreException;

import javax.persistence.EntityManager;
import java.util.List;

public class ModelDaoImpl extends ComplexJpaRepository<Model, Long> implements ModelDao {

    public ModelDaoImpl(EntityManager entityManager) {
        super(Model.class, entityManager);
    }

    @Override
    public List<Model> findByUngrouped() {
        throw new IgnoreException(" No SQL ");
    }

    @Override
    public List<Model> findAllByTypesWithMetadataAndFields(ModelType... types) {
        return super.findAll(PropertyFilter.builder().in("type", types).build());
    }

}
