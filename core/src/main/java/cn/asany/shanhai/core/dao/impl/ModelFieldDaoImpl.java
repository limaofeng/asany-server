package cn.asany.shanhai.core.dao.impl;

import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.dao.ModelFieldDao;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

/**
 * @author limaofeng
 */
public class ModelFieldDaoImpl extends ComplexJpaRepository<ModelField, Long> implements ModelFieldDao {

    private  EntityManager entityManager;

    public ModelFieldDaoImpl(EntityManager entityManager) {
        super(ModelField.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<ModelField> findWithModelAndType(List<PropertyFilter> filters) {
        return this.findAll(filters);
    }

    @Override
    public List<ModelField> findByUngrouped() {
        return Collections.emptyList();
    }

}
