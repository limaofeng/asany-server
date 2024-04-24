package cn.asany.shanhai.core.dao.impl;

import cn.asany.shanhai.core.dao.ModelDao;
import cn.asany.shanhai.core.domain.Model;
import jakarta.persistence.EntityManager;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.error.IgnoreException;
import org.springframework.data.domain.Sort;

public class ModelDaoImpl extends SimpleAnyJpaRepository<Model, Long> implements ModelDao {

  public ModelDaoImpl(EntityManager entityManager) {
    super(Model.class, entityManager);
  }

  @Override
  public List<Model> findByUngrouped() {
    throw new IgnoreException(" No SQL ");
  }

  @Override
  public Model getDetails(Long id) {
    return super.findById(id).orElse(null);
  }

  @Override
  public List<Model> findAllWithMetadataAndFields(PropertyFilter filter, Sort orderBy) {
    return super.findAll(filter, orderBy);
  }
}
