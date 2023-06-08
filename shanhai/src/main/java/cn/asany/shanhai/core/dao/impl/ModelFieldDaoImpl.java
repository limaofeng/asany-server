package cn.asany.shanhai.core.dao.impl;

import cn.asany.shanhai.core.dao.ModelFieldDao;
import cn.asany.shanhai.core.domain.ModelField;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

/** @author limaofeng */
public class ModelFieldDaoImpl extends ComplexJpaRepository<ModelField, Long>
    implements ModelFieldDao {

  public ModelFieldDaoImpl(EntityManager entityManager) {
    super(ModelField.class, entityManager);
  }

  @Override
  public List<ModelField> findWithModelAndType(PropertyFilter filter) {
    return this.findAll(filter);
  }

  @Override
  public List<ModelField> findByUngrouped() {
    return Collections.emptyList();
  }
}
