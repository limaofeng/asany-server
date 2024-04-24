package cn.asany.shanhai.core.dao.impl;

import cn.asany.shanhai.core.dao.ModelFieldDao;
import cn.asany.shanhai.core.domain.ModelField;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;

/**
 * @author limaofeng
 */
public class ModelFieldDaoImpl extends SimpleAnyJpaRepository<ModelField, Long>
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
